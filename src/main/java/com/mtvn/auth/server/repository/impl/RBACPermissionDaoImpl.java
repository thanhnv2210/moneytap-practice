package com.mtvn.auth.server.repository.impl;

import com.mtvn.auth.server.model.RBACPermission;
import com.mtvn.auth.server.repository.RBACPermissionDao;
import com.mtvn.persistence.entities.AppRole;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class RBACPermissionDaoImpl implements RBACPermissionDao {
    String[] rbacPermissions = new String[0];

    public RBACPermissionDaoImpl() {
        try {
            log.info("Reading rbac permission from rbacPermission.properties");
            rbacPermissions =
                    IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("rbacPermission.properties")).split("\n");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Exception Reading rbac permission from rbacPermission.properties {}", e);
        }
    }

    @Override
    public List<RBACPermission> getRBACPermissionsFromFile() throws Exception {
        List<RBACPermission> rbacPermissionList = new ArrayList<RBACPermission>();
        List<String> rbacRecordList = getRBACAsString();
        for (String rbacRecord : rbacRecordList) {
            String[] rbacSplit = rbacRecord.trim().split(" ");
            rbacPermissionList.add(getRBACPermission(rbacSplit[0], rbacSplit[1], rbacSplit[2]));
        }
        return rbacPermissionList;
    }

    public List<String> getRBACAsString() {
        List<String> rbacRecord = new ArrayList<String>();
        /** order is very important **/
        // url scope(,) roles(,)
        for (String rbacPermission : rbacPermissions) {
            rbacRecord.add(rbacPermission.trim());
        }
        return rbacRecord;
    }

    public RBACPermission getRBACPermission(String url, String scope, String roleNames) {
        RBACPermission rbacPermission = new RBACPermission();
        rbacPermission.setUrl(url);
        rbacPermission.setScope(scope);
        Set<AppRole> roleSet = new HashSet<AppRole>();
        for (String roleName : roleNames.split(",")) {
            AppRole role = new AppRole();
            role.setName(roleName);
            roleSet.add(role);
        }
        rbacPermission.setRoles(roleSet);
        return rbacPermission;
    }
}
