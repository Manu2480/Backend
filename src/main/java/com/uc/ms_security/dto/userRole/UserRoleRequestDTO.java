package com.uc.ms_security.dto.userRole;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleRequestDTO {
    @NotNull(message = "El usuario es obligatorio")
    @Positive(message = "El ID del usuario debe ser positivo")
    private Long userId;

    @NotNull(message = "El rol es obligatorio")
    @Positive(message = "El ID del rol debe ser positivo")
    private Long roleId;
}