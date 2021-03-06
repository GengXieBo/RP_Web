package com.huaiguang.rpweb.controller;


import com.huaiguang.rpweb.entity.Slide;
import com.huaiguang.rpweb.entity.User;
import com.huaiguang.rpweb.service.SlideService;
import com.huaiguang.rpweb.service.UserService;
import com.huaiguang.rpweb.utils.JnaTest;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

/**
 * <p>
 * 切片表 前端控制器
 * </p>
 *
 * @author lixu
 * @since 2020-12-09
 */
@Controller
public class SlideController {
    public static Map<Integer, Double> rate_map = new HashMap<Integer, Double>();
    public static boolean gpu_use_flag = false;
    static{
        rate_map.put(0, 0.75);
        rate_map.put(1, 0.15);
        rate_map.put(2, 0.10);
    }

    //全局同时只可以跑一个切片，因此一个分数和一个进度条就已经够了
    public static double score = 0;
    public static double progress = 0;

    public static int time_past = 0;
    private static int active_slide_index = 0;

    @GetMapping(value = "/main/compute/{index}")
    public String calucluteSlide(@PathVariable("index") String index, Model model, HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        String userid = (String)req.getSession().getAttribute("userid");
        if(userid==null)return "login";

        SlideService slideService = new SlideService();
        List<Slide> list = slideService.queryAll(userid);

        int active_index = Integer.valueOf(index);

        Slide slide = list.get(active_index);
        String slidepath = slide.getPath();
        String savepath = slide.getResult();

        // ContentType 必须指定为 text/event-stream
        resp.setContentType("text/event-stream");
        // CharacterEncoding 必须指定为 UTF-8
        resp.setCharacterEncoding("UTF-8");
        //计算中设为1
        slideService.updateSlide(slide.getId(), "1", "0");
        try{
            computeSlide(req, resp, slidepath, savepath);
        }catch(Exception e) {
            e.printStackTrace();
        }
        finally{
            slideService.updateSlide(slide.getId(), "0", "0");
        }
        //计算完设为2
        slideService.updateSlide(slide.getId(), "2", String.valueOf(score));

        return "main";
    }

    public static void computeSlide(HttpServletRequest req, HttpServletResponse resp, String slidepath, String savepath) throws IOException {

        if(gpu_use_flag == false)
        {
            gpu_use_flag = true;
            score = 0;
            JnaTest jnaTest = new JnaTest();
            jnaTest.getHandle();
            final PrintWriter pw = resp.getWriter();

            time_past = getSecondTimestamp(new Date());

            JnaTest.RnnLibrary.UpdateProgressFunc myUpdate2 = new JnaTest.RnnLibrary.UpdateProgressFunc() {
                @Override
                public void invoke(int stage, int w, int h, int currentIndex) {
                    //在这里更新resp...
                    try {
                        progress = computeProgress(stage, w, h, currentIndex);
                        int time_current = getSecondTimestamp(new Date());
                        if(time_current - time_past<1) {
                            time_past = time_current;
                            return;
                        }
                        time_past = time_current;
                        System.out.println("progress: " + progress);
                        //为了防止客户端突然间断连
                        if(pw.checkError()){
                            return;
                        }
                        pw.write("data: "+ progress + "\n\n");
                        pw.flush();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            JnaTest.updateProgressFunc = myUpdate2;

            score = jnaTest.slideProcess(10, slidepath, savepath);
            jnaTest.free();
            gpu_use_flag = false;
            try{
                pw.write("event: complete\ndata: slide computation completed\n\n");
                pw.flush();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
        else
        {
            PrintWriter printWriter = resp.getWriter();
            try{
                printWriter.write("event: busy\ndata: the gpu is busy\n\n");
                printWriter.flush();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public static double computeProgress(int stage, int w, int h, int currentIndex){
        double ret = 0;
        switch (stage){
            case 2:
                ret = ret + rate_map.get(stage) * (currentIndex) / (w*h) + rate_map.get(1) + rate_map.get(0);
                break;
            case 1:
                ret = ret + rate_map.get(stage) * (currentIndex) / (w*h) + rate_map.get(0);
                break;
            case 0:
                ret = rate_map.get(stage) * (currentIndex) / (w*h);
                break;
            default:
                ret = 0;
        }
        return ret;
    }

    public static int getSecondTimestamp(Date date){
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime());
        int length = timestamp.length();
        if (length > 3) {
            return Integer.valueOf(timestamp.substring(0,length-3));
        } else {
            return 0;
        }
    }


    @RequestMapping(value = "/main")
    public String main(Model model, HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String userid = (String)req.getSession().getAttribute("userid");
        if(userid==null)return "login";
//        String userid = "aa8143c63e0011ebbd85ac1f6b8ae4f9";

        SlideService slideService = new SlideService();
        List<Slide> slide_list = slideService.queryAll(userid);

        UserService userService = new UserService();
        User user = userService.queryById(userid);
        model.addAttribute("username", user.getUsername().toUpperCase());

        if(slide_list.size()==0){
            return "MainNoSlide";
        }

        // iterate all slides to get scores
        List<List<String>> slide_scores = new ArrayList<>();
        List<String> slide_score = new ArrayList<>();
        List<String> flag_list = new ArrayList<>();
        for(Slide sl : slide_list) {

            String score_path = sl.getResult();
            List<String> scores = new ArrayList<>();
            if(score_path != null){
                // read grade
                String path = score_path + "model2";
                File f = new File(path);
                if(f.exists()){
                    File[] subfiles = f.listFiles();
                    for(int i=0; i<subfiles.length; i++){
                        String[] splits = subfiles[i].getName().split("_");
                        String grade = splits[splits.length - 1];
                        grade = grade.substring(0, grade.lastIndexOf(".jpg"));
                        scores.add(grade);
                    }
                }
            }

            //如果当前结果还没计算出来，则全部置为0
            if(scores.size() == 0){
                for(int i = 0; i < 10; i++){
                    scores.add(String.valueOf(0));
                }
            }
            slide_scores.add(scores);

            String score = sl.getScore();
            slide_score.add(score);

            String flag = sl.getFlag();
            flag_list.add(flag);


        }


        model.addAttribute("active_index", active_slide_index);
        model.addAttribute("slide_list", slide_list);
        model.addAttribute("slide_scores", slide_scores);
        model.addAttribute("slide_score", slide_score);
        model.addAttribute("flag_list", flag_list);

        return "main";
    }


    @RequestMapping(value = "/main/{active_index}/{slide_index}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("active_index") int active_index,
                                           @PathVariable("slide_index") int slide_index,
                                           HttpServletRequest req, HttpServletResponse resp,
                                           Model model) throws Exception {

        String userid = (String)req.getSession().getAttribute("userid");
//        String userid = "aa8143c63e0011ebbd85ac1f6b8ae4f9";
        SlideService slideService = new SlideService();
        List<Slide> list = slideService.queryAll(userid);
        Slide slide = list.get(active_index);
        String slide_path = null;
        String result = slide.getResult();
        int newWidth = 1000;
        int newHeight = 1000;
        if (slide_index == 0) {
            slide_path = result + "thumbnail\\thumbnail.jpg";
            newWidth = 2048;
            newHeight = 2048;
        }
        else {
            String path = result + "model2\\";
            File f = new File(path);
            File subfiles[] = f.listFiles();
            slide_path = path + subfiles[slide_index-1].getName();
        }

        byte[] slide_data = null;
        try {
            InputStream in = new FileInputStream(slide_path);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            BufferedImage prevImage = ImageIO.read(in);
            BufferedImage image = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_BGR);
            Graphics graphics = image.createGraphics();
            graphics.drawImage(prevImage, 0, 0, newWidth, newHeight, null);
            ImageIO.write(image, "jpg", os);
            slide_data = os.toByteArray();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        model.addAttribute("cal_flag", slide.getFlag());
        return new ResponseEntity<>(slide_data, headers, HttpStatus.OK);
    }

    @GetMapping(value = "/main/cal_completed/{slide_index}")
    public void calucluteSlide(@PathVariable("slide_index") String slide_index, HttpServletRequest req, HttpServletResponse resp) throws SQLException, IOException {
        String userid = (String) req.getSession().getAttribute("userid");
        if (userid == null) resp.sendRedirect("/login");

        SlideService slideService = new SlideService();
        List<Slide> slide_list = slideService.queryAll(userid);

        Slide slide = slide_list.get(Integer.valueOf(slide_index));
        String slide_score = slide.getScore();
        String score_path = slide.getResult();
        StringBuilder sb = new StringBuilder();
        if(score_path != null){
            // read grade
            String path = score_path + "model2";
            File f = new File(path);
            if(f.exists()){
                File[] subfiles = f.listFiles();
                for(int i=0; i<subfiles.length; i++){
                    String[] splits = subfiles[i].getName().split("_");
                    String grade = splits[splits.length - 1];
                    grade = grade.substring(0, grade.lastIndexOf(".jpg"));
                    sb.append(grade+" ");
                }
            }
        }

        String flag = slide.getFlag();


        resp.addHeader("slide_score", String.valueOf(score));
        resp.addHeader("patch_scores", sb.toString().trim());
        resp.addHeader("flag", flag);

    }

}

