package com.zw.springcloudserviceprovider.controller;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Slf4j
public class TestController {

    @Autowired
    private ApplicationContext context;

    @Autowired
    private Scheduler scheduler;

    @GetMapping("/getService")
    public String getService(@RequestParam String name){
        log.info("provider get service {}",name);
        return name;
    }


    @GetMapping("/sleep")
    public String sleep() throws InterruptedException {
        log.error("sleep start 12");
        Thread.sleep(30000);
        log.error("sleep end");
        return "wakeup";
    }


    @GetMapping("/systemOut")
    public void systemOut() {
        ConfigurableApplicationContext ctx = (ConfigurableApplicationContext) context;
        ctx.close();
    }


    @GetMapping("/stopScheduler")
    public void stopScheduler() throws SchedulerException {
        if(scheduler.isStarted()){
            scheduler.shutdown();
        }
    }
}
