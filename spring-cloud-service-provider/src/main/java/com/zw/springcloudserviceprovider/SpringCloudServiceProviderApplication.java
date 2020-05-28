package com.zw.springcloudserviceprovider;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Scheduler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableFeignClients
@Slf4j
public class SpringCloudServiceProviderApplication {

    private static Scheduler scheduler;

    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SpringCloudServiceProviderApplication.class, args);
        //程序结束钩子
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @SneakyThrows
            @Override
            public void run() {
                // 在JVM关闭之前执行收尾工作
                // 注意事项：
                // 1.在这里执行的动作不能耗时太久
                // 2.不能在这里再执行注册，移除关闭钩子的操作
                // 3 不能在这里调用System.exit()
                log.info("Application ShutDown...");
                scheduler.shutdown();
            }
        });
    }

}
