package com.uc.ms_security.mapper;

/* El mapper nos permite hacer un proceso de conversión
*
* Nos permiten hacer 3 procesos:
* 1. de la forma DTO a ENTITY
* 2. de la forma ENTITY a DTO*/

import com.uc.ms_security.dto.user.CreateUserDTO;
import com.uc.ms_security.dto.user.UpdateUserDTO;
import com.uc.ms_security.dto.user.UserDetailResponseDTO;
import com.uc.ms_security.dto.user.UserResponseDTO;
import com.uc.ms_security.dto.user.UserRolesResponseDTO;
import com.uc.ms_security.dto.user.UserSessionsResponseDTO;
import com.uc.ms_security.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor //agregue esta linea para poder agregar el private final ProfileMapper
public class UserMapper {

    private final ProfileMapper profileMapper;
    private final SessionMapper sessionMapper;
    private final UserRoleMapper userRoleMapper;

    // Metodo en que entra un DTO y crea una ENTITY
    public User toEntity(CreateUserDTO dto, String encodedPassword) {
        User user = new User(); //instancio, manipulamos la tabla

        //tomamos los datos
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(encodedPassword);

        //retornamos el usuario
        return user;
    }

    // Metodo para actualizar un usuario
    public void updateEntity(UpdateUserDTO dto, User user) {
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
    }

    // Metodo para mostrar un usuario (crea un objeto de tipo respuesta, no manda la contraseña)
    public UserResponseDTO toResponseDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    // traemos el perfil del usuario desde la base
    public UserDetailResponseDTO toDetailResponseDTO(User user) {
        return new UserDetailResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                profileMapper.toResponseDTO(user.getProfile())
        );
    }

    public UserSessionsResponseDTO toSessionsResponseDTO(User user) {
        return new UserSessionsResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                sessionMapper.toResponseDTOList(user.getSessions())
        );
    }

    // este metodo arma el abjeto segun la plantilla que da el dto
    public UserRolesResponseDTO toRolesResponseDTO(User user) {
        return new UserRolesResponseDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                userRoleMapper.toResponseDTOList(user.getUserRoles())
        );
    }

    //Metodo para listar todos los usuarios
    public List<UserResponseDTO> toResponseDTOList(List<User> users) {
        // hay que mapear (es como un casteo)
        return users.stream()
                .map(this::toResponseDTO)
                .toList();
    }
}