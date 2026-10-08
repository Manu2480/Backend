package com.uc.ms_security.dto.userRole;

import com.uc.ms_security.dto.user.UserResponseDTO;
import lombok.Value;

//DTO de salida
//ver los roles que usuarios tienen

@Value
public class RoleUserResponseDTO {
    Long id;
    Long roleId;
    UserResponseDTO user;
}
