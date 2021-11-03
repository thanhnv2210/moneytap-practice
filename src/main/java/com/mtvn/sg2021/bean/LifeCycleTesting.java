package com.mtvn.sg2021.bean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
@Lazy(value = false)
public class LifeCycleTesting {
    @Bean
    BeanTemplate getBeanTemplate(){
        return new BeanTemplate(100, "Thanh", 32);
    }

    @PostConstruct
    private void printData(){
        log.info("getBeanTemplate:{}",  getBeanTemplate());
    }
}
