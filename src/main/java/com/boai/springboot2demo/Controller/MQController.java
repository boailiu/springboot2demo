package com.boai.springboot2demo.Controller;

import com.boai.springboot2demo.Util.FileUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/mq")
public class MQController {

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public MQController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @RequestMapping("/log/test/{logFilePath}")
    public void logTest(@PathVariable String logFilePath) throws IOException {
        logFilePath = "D:\\logs\\test\\ga.log";
        RandomAccessFile logFile = FileUtil.getLogFile(logFilePath);
        if (logFile == null) return;
        String temp;
        while ((temp = logFile.readLine()) != null) {
//            rabbitTemplate.convertAndSend("bootTestQueue", temp.getBytes(StandardCharsets.UTF_8));
            if (!StringUtils.isEmpty(temp)) {
                rabbitTemplate.convertAndSend("bootTestQueue", temp.getBytes(StandardCharsets.UTF_8));
            }
        }

    }
}
