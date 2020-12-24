package com.huaiguang.rpweb.controller;

import com.huaiguang.rpweb.entity.Slide;
import com.huaiguang.rpweb.entity.User;
import com.huaiguang.rpweb.payload.UploadFileResponse;
import com.huaiguang.rpweb.service.SlideService;
import com.huaiguang.rpweb.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class SlidesOpController {

    @GetMapping("/slides")
    public String ShowSlides(Model model, HttpServletRequest req, HttpServletResponse resp) {
        String userid = (String)req.getSession().getAttribute("userid");
        UserService userService = new UserService();
        User user = null;
        try {
            user = userService.queryById(userid);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        SlideService slideService = new SlideService();
        List<Slide> slide_list = new ArrayList<>();
        try {
            slide_list = slideService.queryAll(userid);
            for(int i = 0; i < slide_list.size(); i++) {
                if(slide_list.get(i).getCreateTime() == null) {
                    slide_list.get(i).setCreateTime(new Date());
                }
                if(slide_list.get(i).getScore() == null) {
                    slide_list.get(i).setScore("null");
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        model.addAttribute("username", user.getUsername().toUpperCase());
        model.addAttribute("slide_list", slide_list);
        return "slides";
    }

    @GetMapping(value = "/slides/{slide_id}")
    public String OpSlides(@PathVariable("slide_id") String slide_id,
                           HttpServletRequest req, HttpServletResponse resp) throws SQLException {
        String userid = (String)req.getSession().getAttribute("userid");
        if(userid==null)return "login";

        SlideService slideService = new SlideService();

        slideService.deleteSlide(slide_id);
        // del the slide

        return "redirect:/slides";
    }

}
