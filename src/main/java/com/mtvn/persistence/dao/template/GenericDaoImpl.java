package com.mtvn.persistence.dao.template;

import com.mtvn.persistence.entities.template.GenericEntity;
import com.mtvn.persistence.hibernate.criteria.CustomCriteria;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.criterion.Restrictions;
import org.hibernate.internal.CriteriaImpl;
import org.springframework.core.annotation.AnnotationUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Transactional
public class GenericDaoImpl<E extends GenericEntity<PK>, PK extends Serializable> implements GenericDao<E, PK> {

    @PersistenceContext
    private EntityManager entityManager;

    public E findById(PK id) {
        return findById(id, false);
    }

    public E findById(PK id, boolean includeDeleted) {
        E entity = currentSession().get(getPersistenceClass(), id);
        if (entity == null) {
            return null;
        }
        if(entity.isDeleted() && !includeDeleted)
            return null;
        return entity;
    }

    private Class<E> getPersistenceClass(){
        ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
        Class<E> type = (Class<E>) superClass.getActualTypeArguments()[0];
        return type;
    }

    public List<E> findAll() {
        return createDefaultCriteria().list();
    }

    public E save(E entity) {
        if (entity == null) {
            return null;
        }
        currentSession().save(entity);
        return entity;
    }

    public E update(E entity) {
        return saveOrUpdate(entity);
    }

    public E saveOrUpdate(E entity) {
        if (entity == null) {
            return null;
        }
        currentSession().saveOrUpdate(entity);
        return entity;
    }

    @Override
    public void saveOrUpdateAll(Collection<E> entities) {
        if (entities == null || entities.isEmpty()) {
            return;
        }
        for(E e : entities) {
            saveOrUpdate(e);
        }
    }

    public void delete(E entity) {
        if (entity == null || entity.isDeleted()) {
            return;
        }
        entity.setDeleted(true);
        update(entity);
    }

    public void deleteById(PK id) {
        E e = findById(id);
        delete(e);
    }

    protected Session currentSession() {
        return entityManager.unwrap(Session.class);
    }

    // TODO Why we have a custom function with customer data?
//    protected boolean isCustomerAndApplicationIdNotPresent(Customer customer) {
//        return customer == null || customer.isDeleted() || !StringUtils.hasText(customer.getCurrentApplicationId());
//    }

    protected Criteria createDefaultCriteria(){
        return createCriteria(false, null);
    }

    protected Criteria createDefaultCriteria(String alias){
        return createCriteria(false, alias);
    }

    protected Criteria createCriteria(boolean showDeleted, String alias){
        Criteria criteria = currentSession().createCriteria(getPersistenceClass(), alias);
        CriteriaImpl crit = (CriteriaImpl)criteria;
        criteria = new CustomCriteria(getPersistenceClass().getName(), alias, crit.getSession());
        if (!showDeleted) {
            criteria.add(Restrictions.eq("isDeleted", false));
        }
        Cache cache = AnnotationUtils.findAnnotation(getPersistenceClass(), Cache.class);
        if(cache != null && !Objects.equals(CacheConcurrencyStrategy.NONE, cache.usage()))
            criteria.setCacheable(true);
        return criteria;
    }

    public List<E> findByCriteria(Map<String, Object> crit){
        Criteria c = createDefaultCriteria();
        c.add(Restrictions.allEq(crit));
        return c.list();
    }

    public void hardDelete(E entity) {
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }

    public void hardDelete(Collection<E> entities) {
        for(E e : entities) {
            hardDelete(e);
        }
    }


    //Never use this unless its a life and death situation
    @Deprecated
    protected void flush() {
        currentSession().flush();
    }
}
