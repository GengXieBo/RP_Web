package com.huaiguang.rpweb.controller;

import com.huaiguang.rpweb.entity.User;
import com.huaiguang.rpweb.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

@Controller
public class RegisterController {
    @GetMapping("/register")
    public String index(Model model) {
        model.addAttribute("msg", "ok");
        return "register";
    }

    @PostMapping("/register")
    public String login(HttpServletRequest req, HttpServletResponse resp, Map<String, Object> map, Model model) throws IOException, SQLException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String company_uint = req.getParameter("uint");
        String production_method = req.getParameter("pm");
        String dyeing_method = req.getParameter("dm");
        String scanning_method = req.getParameter("si");

        User user = new User();
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        user.setId(uuid);
        user.setUsername(username);
        user.setPassword(password);
        user.setCompany_unit(company_uint);
        user.setProduction_method(production_method);
        user.setDyeing_method(dyeing_method);
        user.setScanning_method(scanning_method);

        UserService userService = new UserService();

        User userSelect = userService.queryByUsername(username);
        if(user==null){
            userService.insertUser(user);
            return "login";
        }
        else{
            model.addAttribute("msg", "username already exists!");
            return "register";
        }

    }
}
