package com.boai.springboot2demo.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

//@Component
public class RabbitTopicComsumer {

    private static final Logger logger_ = LoggerFactory.getLogger(RabbitTopicComsumer.class);

    @RabbitListener(queues = "topic.message")
    public void process(String message) {
        logger_.info("message:" + message);
    }

    @RabbitListener(queues = "topic.messages")
    public void process2(String message) {
        logger_.info("message2:" + message);
    }
}
