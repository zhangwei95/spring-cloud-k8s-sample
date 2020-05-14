package com.zw.springcloudserviceprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SpringCloudServiceProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudServiceProviderApplication.class, args);
    }

}
