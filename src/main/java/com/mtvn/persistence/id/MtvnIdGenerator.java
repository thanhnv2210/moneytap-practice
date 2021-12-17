package com.mtvn.persistence.id;

import com.mtvn.common.string.StringUtils;
import com.mtvn.persistence.entities.template.BaseEntity;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class MtvnIdGenerator implements IdentifierGenerator, ApplicationContextAware {

    // We cannot autowire this as it will be instantiated by hibernate not by Spring.
    private static TimeBasedIdGeneratorService timeBasedIdGeneratorService;

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        if (object instanceof BaseEntity<?>) {
            BaseEntity<?> be = (BaseEntity<?>) object;
            if (be.getId() != null && StringUtils.hasText(be.getId().toString())) {
                return be.getId();
            }
        }
        return MtvnIdGenerator.timeBasedIdGeneratorService.getUniqueId();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        MtvnIdGenerator.timeBasedIdGeneratorService = applicationContext.getBean(TimeBasedIdGeneratorService.class);
    }
}

