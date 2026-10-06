package com.uc.ms_security.mapper;

import com.uc.ms_security.entity.Permission;
import com.uc.ms_security.entity.Role;
import com.uc.ms_security.entity.RolePermission;
import org.springframework.stereotype.Component;

@Component
public class RolePermissionMapper {

    public RolePermission toEntity(Role role, Permission permission) {
        RolePermission rolePermission = new RolePermission();
        rolePermission.setRole(role);
        rolePermission.setPermission(permission);
        return rolePermission;
    }

    public void updateEntity(RolePermission rolePermission, Role role, Permission permission) {
        rolePermission.setRole(role);
        rolePermission.setPermission(permission);
    }

}