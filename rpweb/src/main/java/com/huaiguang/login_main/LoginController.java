package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
public class LoginController {

    @RequestMapping("/login")
    public String index() {
        return "login";
    }

    @PostMapping(value = "/login")
    public String postLogin(@RequestParam("username") String username,
                            @RequestParam("password") String password,
                            Map<String, Object> map,
                            HttpServletResponse response) throws IOException {
        if(username.equals("admin") && password.equals("admin")){
            Cookie cookie = new Cookie("admin", "insdlsadada");
            response.addCookie(cookie);
            response.sendRedirect("/main/0");
        }

        map.put("msg", "username or password error !");
        return "login";
    }
}
