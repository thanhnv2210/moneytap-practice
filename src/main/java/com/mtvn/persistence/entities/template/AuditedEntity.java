package com.mtvn.persistence.entities.template;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.*;

@Getter
@Setter
@MappedSuperclass
public abstract class AuditedEntity extends BaseEntity<String> {

    @Id
    @GenericGenerator(name="mtvnIdGenerator", strategy="com.mtvn.persistence.id.MtvnIdGenerator")
    @GeneratedValue(generator = "mtvnIdGenerator")
    private String id;

    @Version
    @Column(name="lock_id", columnDefinition="int(11) default 0", nullable=false)
    private Integer lockId = 1;

    @CreatedBy
    @Column(name = "created_by", length=32)
    private String createdBy;

    @LastModifiedBy
    @Column(name = "modified_by", length=32)
    private String modifiedBy;

}
