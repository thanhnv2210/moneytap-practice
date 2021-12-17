package com.mtvn.auth.server.repository;

import com.mtvn.auth.server.model.RBACPermission;

import java.util.List;

public interface RBACPermissionDao {
    List<RBACPermission> getRBACPermissionsFromFile() throws Exception;
}
