package com.mtvn.persistence.entities.auth;

import com.mtvn.persistence.entities.template.BaseEntityIntID;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "mt_idm_user_group")
@Data
public class UserGroup extends BaseEntityIntID {
    @Column(name = "username")
    private String username;
    @Column(name = "group_name")
    private String groupName;
}
