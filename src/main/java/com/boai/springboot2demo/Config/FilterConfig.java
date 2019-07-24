package com.boai.springboot2demo.Config;

import com.boai.springboot2demo.Filter.LogFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    private LogFilter logFilter;

    @Autowired
    public FilterConfig(LogFilter logFilter) {
        this.logFilter = logFilter;
    }

    @Bean
    public FilterRegistrationBean<LogFilter> registerFilter() {
        FilterRegistrationBean<LogFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(logFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("LogFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
