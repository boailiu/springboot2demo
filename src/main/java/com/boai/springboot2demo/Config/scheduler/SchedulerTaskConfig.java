package com.boai.springboot2demo.Config.scheduler;

import com.boai.springboot2demo.Util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
public class SchedulerTaskConfig {

    private static final Logger logger = LoggerFactory.getLogger(SchedulerTaskConfig.class);

    @Async
    @Scheduled(fixedRate = 50000)
    public void getCurrentDate() {
        logger.info("此刻时间:" + DateTimeUtil.formatDateTime("yyyy-MM-dd HH:mm:ss", LocalDateTime.now()));
    }
}
