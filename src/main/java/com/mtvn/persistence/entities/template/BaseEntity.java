package com.mtvn.persistence.entities.template;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author ankan
 */
@TypeDefs({
    @TypeDef(name = "json", typeClass = JsonStringType.class),
    @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity<PK extends Serializable> implements GenericEntity<PK>, Cloneable {
    
	@Column(name = "createdat", nullable = false, updatable = false)
    @CreatedDate private Date createdAt;

    @Column(name = "modifiedat", nullable = false)
    @LastModifiedDate private Date modifiedAt;
    
    @Column(name = "isdeleted", nullable = false)
    private Boolean isDeleted = false;

    @Column(name = "isarchived", nullable = false)
    private Boolean isArchived = false;

    @Override
    public Boolean isDeleted() {
        return isDeleted;
    }

    @Override
    public void setDeleted(Boolean deleted) {
        this.isDeleted = deleted;
    }

    @Override
    public Boolean isArchived() {
        return this.isArchived;
    }

    @Override
    public void setArchived(Boolean archived) {
        this.isArchived = archived;
    }

    @Override
    public Date getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public Date getModifiedAt() {
        return this.modifiedAt;
    }

    @Override
    public void setModifiedAt(Date modifiedAt) {
        this.modifiedAt = modifiedAt;
    }
    
    public String getTableName() {
    	Table table = AnnotationUtils.findAnnotation(this.getClass(), Table.class);
		return table.name();
	}
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @PrePersist
    void onCreate(){
        this.setCreatedAt(new Date());
        this.setModifiedAt(new Date());
    }

    @PreUpdate
    void onUpdate(){
        this.setModifiedAt(new Date());
    }
}
