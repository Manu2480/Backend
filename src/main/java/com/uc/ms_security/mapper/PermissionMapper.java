package com.uc.ms_security.mapper;

import com.uc.ms_security.dto.permission.PermissionRequestDTO;
import com.uc.ms_security.entity.Permission;
import org.springframework.stereotype.Component;

@Component
public class PermissionMapper {

    public Permission toEntity(PermissionRequestDTO dto) {
        Permission permission = new Permission();
        permission.setUrl(dto.getUrl());
        permission.setMethod(dto.getMethod());
        permission.setModel(dto.getModel());
        return permission;
    }

    public void updateEntity(PermissionRequestDTO dto, Permission permission) {
        permission.setUrl(dto.getUrl());
        permission.setMethod(dto.getMethod());
        permission.setModel(dto.getModel());
    }

}