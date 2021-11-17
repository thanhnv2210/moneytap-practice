package com.mtvn.persistence.entities.auth;

import com.mtvn.persistence.entities.template.BaseEntityIntID;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "mt_idm_group")
@Data
public class Group extends BaseEntityIntID {
    @Column(name = "group_name")
    private String groupName;
    @Column(name = "group_description")
    private String groupDescription;
}
