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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.util.*;

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
        computeSlide(req, resp, slidepath, savepath);
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

        SlideService slideService = new SlideService();
        List<Slide> list = slideService.queryAll(userid);

        List<String> slides = new ArrayList<>();
        List<String> flags = new ArrayList<>();
        for(int i=0; i<list.size(); i++){
            slides.add("slide: "+ list.get(i).getPath());
            flags.add(list.get(i).getFlag());
        }

        if(slides.size()==0){
            slides.add("slide: None");
            flags.add("0");
        }

        //获取指定切片,如果数据库中不包含切片，则随机生成一个假切片
        Slide slide;
        if(active_slide_index < list.size()) {
            slide = list.get(active_slide_index);
        }
        else{
            slide = new Slide();
            slide.setPath("None");
            slide.setResult("None");
            slide.setId("None");
            slide.setFlag("0");
        }
        //得到切片计算结果路径
        String result = slide.getResult();
        List<String> scores = new ArrayList<>();
        if(result!=null){
            //读取分数
            String path = result + "model2";
            File f = new File(path);
            if(f.exists()){
                File[] subfiles = f.listFiles();
                for(int i=0; i<subfiles.length; i++){
                    //System.out.println(subfiles[i].getName());
                    String[] splits = subfiles[i].getName().split("_");
                    scores.add(splits[splits.length-1]);
                }
            }
        }

        //如果当前结果还没计算出来，则全部置为0
        if(scores.size()==0){
            for(int i=0; i<10; i++){
                scores.add(String.valueOf(0));
            }

        }

        UserService userService = new UserService();
        User user = userService.queryById(userid);
        model.addAttribute("active_index", active_slide_index);
        model.addAttribute("slide_list", slides);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("scores", scores);
        //if caculated

        model.addAttribute("flag_list", flags);

        return "main";
    }


    @RequestMapping(value = "/main/{active_index}/{slide_index}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("active_index") int active_index,
                                           @PathVariable("slide_index") int slide_index,
                                           HttpServletRequest req, HttpServletResponse resp,
                                           Model model) throws Exception {

        String userid = (String)req.getSession().getAttribute("userid");
        SlideService slideService = new SlideService();
        List<Slide> list = slideService.queryAll(userid);
        Slide slide = list.get(active_index);
        String slide_path = null;
        String result = slide.getResult();
        if (slide_index == 0) {
            slide_path = result + "thumbnail\\thumbnail.jpg";
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
            slide_data = new byte[in.available()];
            in.read(slide_data);
            in.close();
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
}

