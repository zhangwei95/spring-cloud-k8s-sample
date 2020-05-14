package com.zw.springcloud.service.consumer.controller;

import com.zw.springcloud.service.consumer.client.ProviderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @Autowired
    private ProviderClient providerClient;

    @GetMapping("/getService")
    public String getService(@RequestParam String name){
        return providerClient.getService(name);
    }

}
