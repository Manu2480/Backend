package com.uc.ms_security.dto.permission;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PermissionRequestDTO {
    @NotBlank(message = "La URL es obligatoria")
    @Size(max = 255, message = "La URL no puede superar 255 caracteres")
    private String url;

    @NotBlank(message = "El método es obligatorio")
    @Size(max = 255, message = "El método no puede superar 255 caracteres")
    private String method;

    @NotBlank(message = "El modelo es obligatorio")
    @Size(max = 255, message = "El modelo no puede superar 255 caracteres")
    private String model;
}