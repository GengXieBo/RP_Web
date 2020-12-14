package com.huaiguang.rpweb.controller;


import com.huaiguang.rpweb.entity.User;
import com.huaiguang.rpweb.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author lixu
 * @since 2020-12-09
 */
@Controller
public class UserController {


    @GetMapping("/login")
    public String index() {
        return "login";
    }

    @PostMapping("/login")
    public String login(HttpServletRequest req, HttpServletResponse resp, Map<String, Object> map) throws IOException, SQLException {
        UserService userService = new UserService();
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        User user = userService.queryByUsername(username);
        if(user!=null) {
            if (user.getPassword().equals(password)) {
                req.getSession().setAttribute("userid", user.getId());
                //System.out.println(user.getId());
                resp.sendRedirect("/main/0");
            }
        }

        map.put("msg", "username or password error !");

        return "login";
    }

    @GetMapping("logout")
    public String logout(HttpServletRequest req, HttpServletResponse resp){
        req.getSession().removeAttribute("userid");
        return "login";
    }
}

