package com.mtvn.auth.server.model;

import com.mtvn.common.string.StringUtils;
import com.mtvn.persistence.entities.AppRole;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class RBACPermission {

    private String url;
    private Set<AppRole> roles = new HashSet<AppRole>();
    private String scope;

    public String getRolesAsString() {
        if(this.roles != null) {
            Set<String> oRoles = new HashSet<>();
            for(AppRole r : this.roles) {
                oRoles.add(r.getName());
            }
            return StringUtils.collectionToCommaDelimitedString(oRoles);
        }
        return null;
    }

}
