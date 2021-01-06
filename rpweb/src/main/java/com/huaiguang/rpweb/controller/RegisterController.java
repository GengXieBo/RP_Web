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

@Controller
public class RegisterController {
    @GetMapping("/register")
    public String index() {

        return "register";
    }

    @PostMapping("/register")
    public String login(HttpServletRequest req, HttpServletResponse resp, Map<String, Object> map) throws IOException, SQLException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String company_uint = req.getParameter("uint");
        String production_method = req.getParameter("pm");
        String dyeing_method = req.getParameter("dm");
        String scanning_method = req.getParameter("si");

        System.out.println(username);
        System.out.println(password);
        System.out.println(company_uint);
        System.out.println(production_method);
        System.out.println(dyeing_method);
        System.out.println(scanning_method);

        return "register";
    }
}
