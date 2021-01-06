package com.huaiguang.rpweb.controller;

import com.huaiguang.rpweb.exception.FileStorageException;
import com.huaiguang.rpweb.payload.UploadFileResponse;
import com.huaiguang.rpweb.service.SlideService;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import com.huaiguang.rpweb.service.FileStorageService;


@Controller
public class UploadController {

    @PostMapping("/upload_file")
    public UploadFileResponse uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = "E:\\upload";
        String userid = (String)req.getSession().getAttribute("userid");
        if(userid==null)resp.sendRedirect("/login");
//        String userid = "aa8143c63e0011ebbd85ac1f6b8ae4f9";
        Path fileStorageLocation = Paths.get("E:/upload").toAbsolutePath().normalize();
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        // Copy file to the target location (Replacing existing file with the same name)
        Path targetLocation = fileStorageLocation.resolve(fileName);
        try {
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SlideService slideService = new SlideService();
        // uncal flag set to 0
        try {
            slideService.insertSlide(userid, fileName, path, "0", "0");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return null;
    }


}
