package com.example.demo;

import com.sun.xml.internal.bind.v2.runtime.property.StructureLoaderBuilder;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @RequestMapping(value = "/main/{index}")
    public String MainPage(@PathVariable("index") String index, Model model) {
        List<String> slides = new ArrayList<String>();
        for(int i = 0; i < 20; i++) {
            slides.add("slide " + i);
        }

        int active_index = Integer.valueOf(index);
        model.addAttribute("active_index", active_index);
        model.addAttribute("slide_list", slides);

//        if caculated
        model.addAttribute("cal_flag", false);
//
//        int rnn_r = 2, rnn_c = 2;
//        model.addAttribute("rnn_r", rnn_r);
//        model.addAttribute("rnn_c", rnn_c);

        String slide_path = "C:\\Users\\A\\IdeaProjects\\Test\\src\\main\\" +
                "resources\\static\\imgs\\wsi.jpg";
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

        model.addAttribute("slide_img", Base64.encodeBase64(slide_data));
        return "main";
    }

    @PostMapping(value = "/main/{index}")
    public String ChooseFile(Model model) {
        return "geeelo";
    }
}
