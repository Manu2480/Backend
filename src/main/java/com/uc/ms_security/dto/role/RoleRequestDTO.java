package com.uc.ms_security.dto.role;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleRequestDTO {
    @NotBlank(message = "El nombre del rol es obligatorio")
    @Size(max = 255, message = "El nombre del rol no puede superar 255 caracteres")
    private String name;

    @Size(max = 255, message = "La descripción no puede superar 255 caracteres")
    private String description;
}