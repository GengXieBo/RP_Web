package com.huaiguang.rpweb.controller;


import com.huaiguang.rpweb.entity.Slide;
import com.huaiguang.rpweb.entity.User;
import com.huaiguang.rpweb.service.SlideService;
import com.huaiguang.rpweb.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
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

    @RequestMapping(value = "/main/{index}")
    public String main(@PathVariable("index") String index, Model model, HttpServletRequest req, HttpServletResponse resp) throws IOException, SQLException {
        String userid = (String)req.getSession().getAttribute("userid");
        if(userid==null)return "login";
        System.out.println("userid:" + userid);

        SlideService slideService = new SlideService();
        List<Slide> list = slideService.queryAll(userid);
        System.out.println(list);

        List<String> slides = new ArrayList<>();
        for(int i=0; i<list.size(); i++){
            slides.add("slide: "+ list.get(i).getPath());
        }


        int active_index = Integer.valueOf(index);
        UserService userService = new UserService();
        User user = userService.queryById(userid);
        model.addAttribute("active_index", active_index);
        model.addAttribute("slide_list", slides);
        model.addAttribute("username", user.getUsername());
        //if caculated
        model.addAttribute("cal_flag", false);

        return "main";
    }


    @RequestMapping(value = "/main/{active_index}/{slide_index}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("active_index") int active_index,
                                           @PathVariable("slide_index") int slide_index,
                                           HttpServletRequest req, HttpServletResponse resp) throws Exception {

        String userid = (String)req.getSession().getAttribute("userid");
        SlideService slideService = new SlideService();
        slideService.queryByUserid(userid);
        String slide_path = null;
        String result = "E:\\upload\\1100037\\";
        if (slide_index == 0) {
            slide_path = result + "thumbnail\\wsi.jpg";
        }
        else {
            slide_path = result + "model2\\" + slide_index + ".jpg";
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
        return new ResponseEntity<>(slide_data, headers, HttpStatus.OK);
    }
}

