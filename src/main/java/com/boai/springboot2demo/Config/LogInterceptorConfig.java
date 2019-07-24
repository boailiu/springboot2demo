package com.boai.springboot2demo.Config;

import com.boai.springboot2demo.Inteceptor.LogInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 实现类方式进行配置
 *
 * 也可以使用javaBean的方式实现配置
 */
@Configuration
public class LogInterceptorConfig extends WebMvcConfigurationSupport {

    private final LogInterceptor logInterceptor;

    @Autowired
    public LogInterceptorConfig(LogInterceptor logInterceptor) {
        this.logInterceptor = logInterceptor;
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
