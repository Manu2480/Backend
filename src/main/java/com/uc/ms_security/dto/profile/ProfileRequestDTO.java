package com.uc.ms_security.dto.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ProfileRequestDTO {
    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "\\d{10}", message = "El teléfono debe contener exactamente 10 dígitos")
    @Size(max = 30, message = "El teléfono no puede superar 30 caracteres")
    private String phone;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @PastOrPresent(message = "La fecha de nacimiento no puede ser futura")
    private Date birthDate;

}