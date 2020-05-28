package com.zw.springcloudserviceprovider;



import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.undertow.Undertow;
import io.undertow.server.ConnectorStatistics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServer;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author: guoyankui
 * @DATE: 2018/5/20 5:47 PM
 *
 * 优雅关闭 Spring Boot undertow
 */
@Component
@Slf4j
public class GracefulShutdownUndertow  implements ApplicationListener<ContextClosedEvent> {
    @Autowired
    private GracefulShutdownUndertowWrapper gracefulShutdownUndertowWrapper;

    @Autowired
    private ServletWebServerApplicationContext context;

    LoadingCache<Long, AtomicLong> counter= CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.SECONDS)
            .build(new CacheLoader<Long, AtomicLong>() {
                @Override
                public AtomicLong load(Long aLong) throws Exception {
                    return new AtomicLong(0);
                }
            });

    int limit=2;
    int unit=1000;
    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent){
        gracefulShutdownUndertowWrapper.getGracefulShutdownHandler().shutdown();
        try {
            UndertowServletWebServer webServer = (UndertowServletWebServer)context.getWebServer();
            Field field = webServer.getClass().getDeclaredField("undertow");
            field.setAccessible(true);
            Undertow undertow = (Undertow) field.get(webServer);
            List<Undertow.ListenerInfo> listenerInfo = undertow.getListenerInfo();
            Undertow.ListenerInfo listener = listenerInfo.get(0);
            ConnectorStatistics connectorStatistics = listener.getConnectorStatistics();
            while (connectorStatistics.getActiveConnections() > 0){
                Long current = System.currentTimeMillis()/unit;
                if(counter.get(current).incrementAndGet()<limit){
                    log.info("当前连接数："+connectorStatistics.getActiveConnections());
                }
            }
            log.info("当前连接数："+connectorStatistics.getActiveConnections());
        } catch (Exception e){
            // Application Shutdown
        }
    }


}
