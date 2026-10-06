package com.uc.ms_security.dto.user;

//j¿ jakarta es para hacer validaciones
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

//no tiene @id porque esto no va a la base de datos
public abstract class BaseUserDTO {

    @NotBlank(//esto no puede venir en blanco (vacio)
            message = "El nombre es obligatorio"
    )
    @Size(//debe respetar estas longitudes
            min = 2,
            max = 100,
            message = "El nombre debe tener entre 2 y 100 caracteres"
    )
    private String name;

    @NotBlank(
            message = "El email es obligatorio"
    )
    @Email(//este decorador valida por defecto el email
            message = "El email no tiene un formato válido"
    )
    private String email;
}
