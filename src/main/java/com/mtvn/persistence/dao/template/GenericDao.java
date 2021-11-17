package com.mtvn.persistence.dao.template;

import com.mtvn.persistence.entities.template.GenericEntity;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface GenericDao<E extends GenericEntity<PK>, PK extends Serializable> {
    E findById(PK id);
    E findById(PK id, boolean includeDeleted);
    List<E> findAll();
    E save(E entity);
    E update(E entity);
    E saveOrUpdate(E entity);
    void saveOrUpdateAll(Collection<E> entities);
    void delete(E entity);
    void deleteById(PK id);
    List<E> findByCriteria(Map<String, Object> crit);
    void hardDelete(E entity);
    void hardDelete(Collection<E> entities);
}
