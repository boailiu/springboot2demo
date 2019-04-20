package com.boai.springboot2demo.Config;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.host}")
    private String host;

    @Value("${spring.rabbitmq.port}")
    private int port;

    @Value("${spring.rabbitmq.username}")
    private String userName;

    @Value("${spring.rabbitmq.password}")
    private String password;

    private String queueName = "bootTestQueue";




    @Bean
    public Queue bootTestQueue(){
        return new Queue("bootTestQueue");
    }

    @Bean
    public ConnectionFactory connectionFactory(){
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(userName);
        factory.setPassword(password);
        factory.setHost(host);
        factory.setPort(port);

        return factory;
    }

    @Bean
    public Channel rabbitChannel(){
        ConnectionFactory factory = connectionFactory();
        Channel channel = null;
        try {
            Connection conn = factory.newConnection();
            channel = conn.createChannel();
            channel.queueDeclare(queueName, true, false, false, null);
            channel.confirmSelect();
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
            return channel;
        }
        return channel;

    }
}
