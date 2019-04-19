package com.boai.springboot2demo.Config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {



    @Bean
    public Queue bootTestQueue(){
        return new Queue("bootTestQueue");
    }
}
