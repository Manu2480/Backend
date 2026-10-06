package com.uc.ms_security.mapper;

import com.uc.ms_security.dto.role.RoleRequestDTO;
import com.uc.ms_security.entity.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper {

    public Role toEntity(RoleRequestDTO dto) {
        Role role = new Role();
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        return role;
    }

    public void updateEntity(RoleRequestDTO dto, Role role) {
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
    }

}