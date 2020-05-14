package com.zw.springcloud.service.consumer.client.fallback;

import com.zw.springcloud.service.consumer.client.ProviderClient;
import org.springframework.stereotype.Component;

/**
 * Feign Fallback
 * @author zhangwei
 * @date 2020/05/14
 */
@Component
public class ProviderFallback implements ProviderClient {
    @Override
    public String getService(String name) {
        return "get Service fallback";
    }
}
