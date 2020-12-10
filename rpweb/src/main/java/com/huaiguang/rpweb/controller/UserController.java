package com.huaiguang.rpweb.controller;


import com.huaiguang.rpweb.entity.User;
import com.huaiguang.rpweb.service.UserService;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author lixu
 * @since 2020-12-09
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @GetMapping("select")
    public String getUser(Model model) throws IOException, SQLException {
        UserService userService = new UserService();
        String id = "d661f2393a8811eb9b0600155d78a40d";
        User user = userService.queryById(id);

        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
        return "ok";
    }

    @PostMapping("/login")
    public String login(Model model, HttpServletRequest req, HttpServletResponse resp) throws IOException{
        String name = req.getParameter("username");
        String password = req.getParameter("password");
        System.out.println(name);
        System.out.println(password);

        return "index";
    }
}

