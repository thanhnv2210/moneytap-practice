package com.mtvn.persistence.entities.auth;

import com.mtvn.persistence.entities.template.BaseEntityIntID;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "mt_idm_role")
@Data
public class Role extends BaseEntityIntID {
    @Column(name = "role_name")
    private String roleName;
    @Column(name = "role_description")
    private String roleDescription;
}
