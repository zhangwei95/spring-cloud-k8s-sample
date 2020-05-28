//package com.zw.springcloudserviceprovider.config;
//
//import com.zw.springcloudserviceprovider.GracefulShutdownTomcat;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
//import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class GracefulShutdownConfig {
//
//    @Autowired
//    private GracefulShutdownTomcat gracefulShutdownTomcat;
//
//    @Bean
//    public ServletWebServerFactory servletContainer(){
//        TomcatServletWebServerFactory tomcatServletWebServerFactory = new TomcatServletWebServerFactory();
//        tomcatServletWebServerFactory.addConnectorCustomizers(gracefulShutdownTomcat);
//        return tomcatServletWebServerFactory;
//    }
//}
