package com.mtvn.persistence.entities;

import com.mtvn.persistence.entities.template.BaseEntityIntID;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "role")
@EqualsAndHashCode(callSuper = true)
@Data
public class AppRole extends BaseEntityIntID {
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
}
