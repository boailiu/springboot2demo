package com.boai.springboot2demo.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/file")
public class FileUploadController {

    private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

    @PostMapping("/uploadFile")
    public Map<String, Object> uploadFile(@RequestParam MultipartFile file) throws IOException {
        Map<String, Object> map = new HashMap<>();
        logger.info("fileName :" + file.getName());
        file.transferTo(new File("D://test" + file.getOriginalFilename()));
        map.put("code", 2000);
        return map;
    }
}
