package com.springboot.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;

@RestController
@RequestMapping("/file")
public class FileController {
    Logger logger = LoggerFactory.getLogger(FileController.class);
    @PostMapping("/single")
    public String uploadSingle(@RequestParam("image") MultipartFile file) throws IOException {
        logger.info("Name : {}", file.getName());
        logger.info("File Size : {}", file.getSize());
        logger.info("Content Type : {}", file.getContentType());
        logger.info("Original File Name {}",file.getOriginalFilename());


//        file.getInputStream();
//        InputStream inputStream=file.getInputStream();
//        FileOutputStream fileOutputStream=new FileOutputStream("data.png");
//        byte[] buffer=new byte[inputStream.available()];
//        fileOutputStream.write(buffer);

        return "file uploaded";
    }

    @PostMapping("/multiple")
    public String uploadMultiple(@RequestParam("images") MultipartFile[] files) throws IOException {

        Arrays.stream(files).forEach(file -> {
            logger.info("Name : {}", file.getName());
            logger.info("File Size : {}", file.getSize());
            logger.info("Content Type : {}", file.getContentType());
            logger.info("Original File Name {}",file.getOriginalFilename());
            System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
        });


        return "Handling multiple files";
    }

//    serving image files in response
    @GetMapping("/serve-image")
    public void serveImageHandler(HttpServletRequest request, HttpServletResponse response){
        System.out.println("serving image");
        try {
            FileInputStream fileInputStream = new FileInputStream("images/rashi.png");
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            StreamUtils.copy(fileInputStream,response.getOutputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

