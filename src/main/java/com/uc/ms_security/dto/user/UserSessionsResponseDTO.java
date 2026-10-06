package com.uc.ms_security.dto.user;

import com.uc.ms_security.dto.session.SessionResponseDTO;
import lombok.Value;

import java.util.List;

@Value
public class UserSessionsResponseDTO {
    Long id;
    String name;
    String email;
    /* UserDetailResponseDTO podria tener esta lista de secciones pero 
    no es recomnedable por la cantidad de joins */
    List<SessionResponseDTO> sessions;
}
