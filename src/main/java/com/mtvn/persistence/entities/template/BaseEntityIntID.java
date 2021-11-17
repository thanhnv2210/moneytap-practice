package com.mtvn.persistence.entities.template;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * @author ankan
 */
@MappedSuperclass
public abstract class BaseEntityIntID extends BaseEntity<Integer> {
	
	private static final long serialVersionUID = -3688403909982714871L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

        
    @Override
	public Integer getId() {
		return id;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}
	
}
