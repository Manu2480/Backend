package com.uc.ms_security.dto.rolePermission;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RolePermissionRequestDTO {
    @NotNull(message = "El rol es obligatorio")
    @Positive(message = "El ID del rol debe ser positivo")
    private Long roleId;

    @NotNull(message = "El permiso es obligatorio")
    @Positive(message = "El ID del permiso debe ser positivo")
    private Long permissionId;
}