package com.uc.ms_security.dto.userRole;

import com.uc.ms_security.dto.role.RoleResponseDTO;
import lombok.Value;

//DTO de salida
//ver usuarios que roles tienen

@Value
public class UserRoleResponseDTO {
    Long id;
    Long userId;
    RoleResponseDTO role;
}