package com.boai.springboot2demo.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class EmailUtil {

    private static final Logger logger = LoggerFactory.getLogger(EmailUtil.class);

    public static void sendMail(String emailAddress, String subject, String content) {
        Properties properties = new Properties();
//        InputStream is = EmailUtil.class.getClassLoader().getResourceAsStream("conf/datasource/email.properties");

        try {
            InputStream is = new ClassPathResource("conf/datasource/email.properties").getInputStream();
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Session session = Session.getInstance(properties);
        try {
            MimeMessage message = new MimeMessage(session);
            String personal = "gamma-monitor";
            message.setFrom(new InternetAddress("userservice@che300.com",
                                                personal,
                                                "UTF-8"));
            String[] addressArray = emailAddress.split("[,;]");
            Address[] tos = new Address[addressArray.length];
            for (int i = 0; i < addressArray.length; i++) {
                InternetAddress internetAddress = new InternetAddress(addressArray[i]);
                tos[i] = internetAddress;
            }
            message.setRecipients(Message.RecipientType.TO, tos);
            message.setSubject(subject, "utf-8");
            message.setContent(content, "text/html;charset=utf-8");

            Transport transport = session.getTransport();
            transport.connect("userservice@che300.com", "Baiyou@a1b2");
            transport.sendMessage(message, message.getAllRecipients());

            transport.close();
        } catch (UnsupportedEncodingException | MessagingException e) {
            logger.error("发送邮件失败", e);
        }
    }

    public static void main(String[] args) {
        sendMail("18810901242@163.com,baliu@che300.com", "test", "test");
    }
}
