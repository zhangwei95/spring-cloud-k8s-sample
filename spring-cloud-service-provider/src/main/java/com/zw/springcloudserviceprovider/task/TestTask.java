package com.zw.springcloudserviceprovider.task;


import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Component;

@Component
@Slf4j
@EnableScheduling
public class TestTask {

    @Scheduled(cron = "*/1 * * * * *")
    public void startJob() throws InterruptedException {
        log.info("test Task "+System.currentTimeMillis()+" Running");
        Thread.sleep(10000);
        log.info("test Task "+System.currentTimeMillis()+" Finish");
    }

}
