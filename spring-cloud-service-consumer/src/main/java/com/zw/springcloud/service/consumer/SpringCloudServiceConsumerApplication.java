package com.zw.springcloud.service.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SpringCloudServiceConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudServiceConsumerApplication.class, args);
    }

}
