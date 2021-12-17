package com.mtvn.persistence.entities;

import com.mtvn.common.string.StringUtils;
import com.mtvn.enums.AuthenticationType;
import com.mtvn.persistence.dao.MtvnUserDetail;
import com.mtvn.persistence.entities.template.AuditedEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.persistence.*;
import java.util.*;

@Entity
@Table( name = "auth_user", uniqueConstraints = {
        @UniqueConstraint(name="user_uk1", columnNames={"username"}),
        @UniqueConstraint(name="user_uk2", columnNames={"phone"}),
        @UniqueConstraint(name="user_uk3", columnNames={"email"})
})
@Getter
@Setter
public class AuthUser extends AuditedEntity implements MtvnUserDetail {

    private static final long serialVersionUID = 1L;

    @Column(name="username", length=256)
    private String username;

    @Column(name="phone", length=16)
    private String phone;

    @Column(name = "auth_type")
    @Enumerated(EnumType.STRING)
    private AuthenticationType authType;

    @Column(name="email", length=256)
    private String email;

    @Column(name = "password", length=256)
    private String password;

    @Column(name="first_name", length=256)
    private String firstName;

    @Column(name="last_name", length=256)
    private String lastName;

    @Column(name="roles", length=512)
    private String roles;

    @Column(name = "account_enabled")
    private boolean enabled = true;

    @Column(name = "account_expired")
    private boolean accountExpired = false;

    @Column(name = "account_locked")
    private boolean accountLocked = false;

    @Column(name = "credentials_expired")
    private boolean credentialsExpired = false;

    @Column(name = "last_login_date")
    private Date lastLoginDate;

    public void setUserName(String username) {
        this.username = StringUtils.hasText(username) ? StringUtils.sanitizeTextField(username).toLowerCase() : username;
    }

    @Override
    @Transient
    public boolean isAccountNonExpired() {
        return !isAccountExpired();
    }

    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }

    @Override
    @Transient
    public boolean isAccountNonLocked() {
        return !isAccountLocked();
    }

    public Set<String> getRolesAsSet(){
        Set<String> roles = new TreeSet<>();
        if(StringUtils.hasText(this.roles))
            roles = StringUtils.commaDelimitedListToSet(this.roles);
        return roles;
    }

    public void addRole(String role) {
        if(StringUtils.hasText(role)) {
            Set<String> roles = getRolesAsSet();
            roles.add(role);
            this.roles = StringUtils.collectionToCommaDelimitedString(roles);
        }
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Set<String> roles = getRolesAsSet();
        for(String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}
