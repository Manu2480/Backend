package com.uc.ms_security.dto.user;

import lombok.Value;

@Value

//como se van a mostrar las respuestas a un usuario (no envio las contraseñas por seguridad)
public class UserResponseDTO {
    Long id;
    String name;
    String email;
}