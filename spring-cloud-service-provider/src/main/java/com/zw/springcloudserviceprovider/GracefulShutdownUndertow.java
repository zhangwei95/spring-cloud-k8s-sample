package com.zw.springcloudserviceprovider;



import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.undertow.Undertow;
import io.undertow.server.ConnectorStatistics;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final static Logger logger = LoggerFactory.getLogger(GracefulShutdownUndertow.class);

    @Autowired
    private GracefulShutdownUndertowWrapper gracefulShutdownUndertowWrapper;

    @Autowired
    private ServletWebServerApplicationContext context;

    private  LoadingCache<Long, AtomicLong> counter = CacheBuilder.newBuilder()
            .expireAfterWrite(2, TimeUnit.MINUTES)
            .build(new CacheLoader<Long, AtomicLong>() {
                @Override
                public AtomicLong load(Long aLong) throws Exception {
                    return new AtomicLong(0);
                }
            });

    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        // 自旋次数
        int limit = 60;
        // 天
        int dayUnit = 1000 * 60 * 60 * 24;
        // 秒
        int secondUnit = 1000 * 60 * 60 * 24;
        //自旋等待时间
        int waitTime = 1000;
        gracefulShutdownUndertowWrapper.getGracefulShutdownHandler().shutdown();
        try {
            UndertowServletWebServer webServer = (UndertowServletWebServer) context.getWebServer();
            Field field = webServer.getClass().getDeclaredField("undertow");
            field.setAccessible(true);
            Undertow undertow = (Undertow) field.get(webServer);
            List<Undertow.ListenerInfo> listenerInfo = undertow.getListenerInfo();
            Undertow.ListenerInfo listener = listenerInfo.get(0);
            ConnectorStatistics connectorStatistics = listener.getConnectorStatistics();
            Long current = System.currentTimeMillis() / secondUnit;

            // 每隔1秒检测是否已经处理完停止服务之前接收的request
//            while (connectorStatistics !=null && connectorStatistics.getActiveRequests() > 0) {
//                if (null != connectorStatistics) {
//                    current = System.currentTimeMillis() / secondUnit;
//                    //每秒输出一次
//                    if (counter.get(current).incrementAndGet() < 2) {
//                        logger.error("Can't shutdown undertow, requests still processing. And there are {} activeConnections...",
//                                connectorStatistics.getActiveRequests());
//                    }
//                    // 超过最大自旋限制 强制退出
//                    if (counter.size() > limit) {
//                        logger.error("shutdown undertow.");
//                        break;
//                    }
//                } else {
//                    logger.error("Can shutdown undertow.");
//                    break;
//                }
//            }
//            logger.error("shutdown undertow.");

            // 60秒无法结束 强制结束
//            while (!gracefulShutdownUndertowWrapper.getGracefulShutdownHandler().awaitShutdown(waitTime)) {
//                //每秒输出一次
//                logger.error("Can't shutdown undertow, requests still processing. And there are {} activeConnections...",
//                            connectorStatistics.getActiveRequests());
//            }

            // 当前请求数大于0 自旋等待处理
            while (connectorStatistics.getActiveConnections() > 0) {
                logger.error("current active connections：" + connectorStatistics.getActiveConnections()
                        + "active requests：" + connectorStatistics.getActiveRequests() );
            }

//            if (connectorStatistics != null) {
//                logger.error("当前请求数：" + connectorStatistics.getActiveRequests());
//            } else {
//                logger.error("当前没有请求");
//            }
            logger.error("shutdown undertow.");
        } catch (Exception e) {
            // Application Shutdown
        }
    }



}
