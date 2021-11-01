package com.mtvn.common.utilities;

import com.mtvn.application.PartnerCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

@Service
public class MTSpringContext implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(MTSpringContext.class);

    private static ApplicationContext context;
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        MTSpringContext.context = context;
    }

    public static<T> T getBean(String beanName, Class<T> clazz){
        try{
            return context != null ? context.getBean(beanName, clazz) : null;
        }catch(NoSuchBeanDefinitionException e){
            logger.trace("No Bean Found For " + beanName);
        }
        return null;
    }

    public static <T> T getBean(Class<T> clazz){
        try{
            return context != null ? context.getBean(clazz) : null;
        }catch(NoSuchBeanDefinitionException e){
            logger.trace("No Bean Found For " + clazz);
        }
        return null;
    }

    public static <T> T getPartnerBean(PartnerCode partnerCode, Class<T> clazz) {
        T t = null;
        try{
            t = context != null ? context.getBean(partnerCode.name().toLowerCase() + clazz.getSimpleName(), clazz) : null;
        }catch(NoSuchBeanDefinitionException e){
            logger.trace("No Bean Found For " + partnerCode);
            try {
                if(t == null) {
                    t = context != null ? context.getBean("default" + clazz.getSimpleName(), clazz) : null;
                }
            }catch(NoSuchBeanDefinitionException e2){
                logger.trace("No Bean Found For " + partnerCode);
            }
        }
        return t;
    }

}
