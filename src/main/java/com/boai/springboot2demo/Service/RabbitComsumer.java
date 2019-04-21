package com.boai.springboot2demo.Service;

import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RabbitListener(queues = "bootTestQueue")
public class RabbitComsumer {

    private static final Logger logger_ = LoggerFactory.getLogger(RabbitComsumer.class);

    @RabbitHandler
    public void process(byte[] message) {
        logger_.info("message:" + new String(message));
    }

/*    @RabbitHandler
    public void process(Message message, Channel channel) {
        try {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
            logger_.info("message:" + new String(message.getBody()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/
}
