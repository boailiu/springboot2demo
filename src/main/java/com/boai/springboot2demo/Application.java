package com.boai.springboot2demo;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("com.boai.springboot2demo.Mapper")
@EnableScheduling
@NacosPropertySource(dataId = "example",autoRefreshed = true)
//@NacosConfigurationProperties(dataId = "springboot2demo",groupId = "demo",autoRefreshed = true)
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
