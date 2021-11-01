package com.mtvn.common.utilities;

import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;

import java.util.Optional;

public class LoggingUtil {

    public static void addMdcParam(String key, String value) {
        MDC.put(key, value);
    }

    public static void removeMdcParam(String key) {
        MDC.remove(key);
    }

    public static Optional<String> getMdcParam(String key) {
        return Optional.ofNullable(MDC.get(key));
    }

    public static HttpHeaders getLoggingInfoMap( ){
        Optional<String> currentUser = LoggingUtil.getMdcParam("current_user");
        Optional<String> currentComponent = LoggingUtil.getMdcParam("current_component");
        HttpHeaders headers = new HttpHeaders();
        if(currentUser.isPresent())
            headers.add("customer-id", currentUser.get());
        if(currentComponent.isPresent())
            headers.add("req-context", currentComponent.get());
        return headers;
    }
}