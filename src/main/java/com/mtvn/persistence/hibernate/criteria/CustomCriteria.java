package com.mtvn.persistence.hibernate.criteria;

import com.mtvn.persistence.entities.template.BaseEntity;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.internal.CriteriaImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

public class CustomCriteria extends CriteriaImpl {

    private static final long serialVersionUID = -1754926103954889754L;

    public CustomCriteria(String entityClassName, SharedSessionContractImplementor session) {
        super(entityClassName, session);
    }

    public CustomCriteria(String entityClassName, String alias, SharedSessionContractImplementor session) {
        super(entityClassName, alias, session);
    }

    @Override
    public List<BaseEntity<?>> list() throws HibernateException {
        long startTime = new Date().getTime();
        int size = 0;
        try{
            List<BaseEntity<?>> retVal = super.list();
            if(retVal != null)
                size = retVal.size();
            return retVal;
        }finally{
            long totalTime = new Date().getTime() - startTime;
            if(totalTime > 500) {
                String message = getEntityOrClassName() + ":list()=" + size + " citeria:" + this.toString();
                logSlowQueryTiming(this.getClass(), message, totalTime);
            }
        }
    }

    @Override
    public Object uniqueResult() throws HibernateException {
        long startTime = new Date().getTime();
        try{
            return super.uniqueResult();
        }finally{
            long totalTime = new Date().getTime() - startTime;
            if(totalTime > 500) {
                String message = getEntityOrClassName() + ":uniqueResult() citeria:" + this.toString();
                logSlowQueryTiming(this.getClass(), message, totalTime);
            }
        }
    }

    private static void logSlowQueryTiming(Class<?> clazz, String message, long totalTime) {
        Logger logger = LoggerFactory.getLogger(clazz);
        logger.warn("QUERY IS SLOW. Time Taken (ms.): {}, criteria: {}", totalTime, message);
    }
}
