package com.boai.springboot2demo.Filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

public class LogFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(LogFilter.class);

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        log.info("request ip:" + servletRequest.getRemoteAddr());
        filterChain.doFilter(servletRequest, servletResponse);
        long end = System.currentTimeMillis();
        log.info("cost time :" + (end - start));
    }
}
