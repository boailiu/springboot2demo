package com.boai.springboot2demo.Config;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    private static final Logger logger_ = LoggerFactory.getLogger(RabbitMQConfig.class);

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

    @Bean
    public CachingConnectionFactory getConn(){
        CachingConnectionFactory conn = new CachingConnectionFactory();
        conn.setAddresses(host +":" + port);
        conn.setUsername(userName);
        conn.setPassword(password);
        conn.setPublisherConfirms(true);
        return conn;
    }

    @Bean
    public SimpleMessageListenerContainer messageContainer(){
        SimpleMessageListenerContainer container =
                new SimpleMessageListenerContainer(getConn());
        String queueName2 = "bootTestQueue2";
        container.setQueueNames(queueName, queueName2);
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(10);
        container.setConcurrentConsumers(1);
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener((ChannelAwareMessageListener) (message, channel) -> {
            /*
             通过basic.qos方法设置prefetch_count=1，这样RabbitMQ就会使得每个Consumer在同一个时间点最多处理一个Message，
             换句话说,在接收到该Consumer的ack前,它不会将新的Message分发给它
             */
            channel.basicQos(1);
            byte[] body = message.getBody();
            logger_.info("message : " + new String(body));
            /*为了保证永远不会丢失消息，RabbitMQ支持消息应答机制。
             当消费者接收到消息并完成任务后会往RabbitMQ服务器发送一条确认的命令，然后RabbitMQ才会将消息删除。*/
            channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
        });
        return container;
    }
}
