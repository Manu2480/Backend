package com.uc.ms_security.dto.user;

import com.uc.ms_security.dto.profile.ProfileResponseDTO;
import lombok.Value;

/*
*  Creamos este dto para devolver el usuario junto con su perfil
*  Es decir, es para ver detalles, mientras que el otro solo muestra
*  el usuario de forma general
*/
@Value
public class UserDetailResponseDTO {

    Long id;

    String name;

    String email;

    ProfileResponseDTO profile;
}