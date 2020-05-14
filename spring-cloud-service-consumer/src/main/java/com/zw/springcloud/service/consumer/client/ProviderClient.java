package com.zw.springcloud.service.consumer.client;


import com.zw.springcloud.service.consumer.client.fallback.ProviderFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Service Provider feign Client
 * @author zhangwei
 * @date 2020/05/14
 */
@FeignClient(name = "http://service-provider:18000", url = "http://service-provider:18000", fallback = ProviderFallback.class)
public interface ProviderClient {
    /**
     * 测试接口
     * @param name
     * @return
     */
    @GetMapping("/api/getService")
    String getService(@RequestParam String name);


}
