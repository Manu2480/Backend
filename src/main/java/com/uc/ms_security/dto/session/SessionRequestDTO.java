package com.uc.ms_security.dto.session;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SessionRequestDTO {
    @NotBlank(message = "El token es obligatorio")
    private String token;

    @NotNull(message = "La fecha de expiración es obligatoria")
    private Date expiration;

    private String code2FA;

    @NotNull(message = "El usuario es obligatorio")
    @Positive(message = "El ID del usuario debe ser positivo")
    private Long userId;
}