package com.example.demo;

import com.sun.xml.internal.bind.v2.runtime.property.StructureLoaderBuilder;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.*;
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

//        String slide_path = "C:\\Users\\A\\IdeaProjects\\Test\\src\\main\\" +
//                "resources\\static\\imgs\\wsi.jpg";
//        byte[] slide_data = null;
//        try {
//            InputStream in = new FileInputStream(slide_path);
//            slide_data = new byte[in.available()];
//            in.read(slide_data);
//            in.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        model.addAttribute("slide_img", Base64.encodeBase64(slide_data));
        return "main";
    }

    @RequestMapping(value = "/main/{active_index}/{slide_index}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(@PathVariable("active_index") int active_index,
                                           @PathVariable("slide_index") int slide_index) throws Exception {
        String slide_path = null;
        if (slide_index == 0) {
            slide_path = "C:\\Users\\A\\IdeaProjects\\Test\\src\\main\\" +
                    "resources\\static\\imgs\\wsi.png";
        }
        else {
            slide_path = "C:\\Users\\A\\IdeaProjects\\Test\\src\\main\\" +
                    "resources\\static\\imgs\\sub_imgs\\" + slide_index + ".png";
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
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(slide_data, headers, HttpStatus.OK);
    }
}
