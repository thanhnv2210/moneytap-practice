package com.mtvn.rest.config;

import com.netflix.config.DynamicPropertyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@ComponentScan(RestConfiguration.BASE_PACKAGE)
public class RestConfiguration {
    public static final String BASE_PACKAGE = "com.mtvn";
    public static int getPortToRun() {
        return DynamicPropertyFactory.getInstance().getIntProperty("PortToRun", 8080).getValue();
    }
    public static int getMinimumJettyThreads() {
        return DynamicPropertyFactory.getInstance().getIntProperty("MinimumJettyThreads",10).getValue();
    }
    public static int getMaximumJettyThreads() {
        return DynamicPropertyFactory.getInstance().getIntProperty("MaximumJettyThreads",1000).getValue();
    }
    public static int getIdleTimeoutJettyThreadsInMillis() {
        return DynamicPropertyFactory.getInstance().getIntProperty("IdleTimeoutJettyThreadsInMillis",60 * 1000).getValue();
    }
}
