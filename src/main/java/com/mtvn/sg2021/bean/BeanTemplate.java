package com.mtvn.sg2021.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"age"})
public class BeanTemplate implements InitializingBean, DisposableBean {
    private Integer customerId;
    private String name;
    private Integer age;

    @PostConstruct
    public void init() {
        System.out.println(
                "[1].Bean HelloWorld has been "
                        + "instantiated and I'm the "
                        + "init() method PostConstruct");
    }

    @Override
    public void destroy() {
        System.out.println(
                "[4].Container has been closed "
                        + "and I'm the destroy() method");
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println(
                "[2].Bean HelloWorld has been "
                        + "instantiated and I'm the "
                        + "init() method");
    }

    @PreDestroy
    public void destroyPre() {
        System.out.println(
                "[3].Container has been closed "
                        + "and I'm the destroy() method PRE");
    }
}
