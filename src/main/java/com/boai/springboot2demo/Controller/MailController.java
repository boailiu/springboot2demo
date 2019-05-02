package com.boai.springboot2demo.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/mail")
public class MailController {

    private static final Logger logger = LoggerFactory.getLogger(MailController.class);

    private JavaMailSender javaMailSender;

    @Autowired
    public MailController(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @GetMapping("/sendMail/{mailContent}")
    public Map<String, Object> sendMail(@PathVariable String mailContent) {
        Map<String, Object> map = new HashMap<>();
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("18810901242@163.com");
        simpleMailMessage.setTo("18810901242@163.com");
        simpleMailMessage.setSubject("来自springbootdemo的测试邮件");
        simpleMailMessage.setText(mailContent);
        javaMailSender.send(simpleMailMessage);
        logger.info("测试邮件发送成功...");
        return map;
    }
}
