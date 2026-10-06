package com.uc.ms_security.service;

import com.uc.ms_security.dto.user.CreateUserDTO;
import com.uc.ms_security.dto.user.UpdateUserDTO;
import com.uc.ms_security.dto.user.UserDetailResponseDTO;
import com.uc.ms_security.dto.user.UserResponseDTO;
import com.uc.ms_security.dto.user.UserSessionsResponseDTO;
import com.uc.ms_security.entity.User;
import com.uc.ms_security.exception.ApplicationException;
import com.uc.ms_security.exception.ErrorCase;
import com.uc.ms_security.mapper.UserMapper;
import com.uc.ms_security.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service //decorador de los servicios
/* esto dice que necesita un constructor*/
@RequiredArgsConstructor
public class UserService {

    /* Empieza la inyección de dependencias (final es una constante en java)*/
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // llega el DTO de la creación (si algo fallo ahí rechaza la petición)
    public UserResponseDTO create(CreateUserDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) { //verifique si existe (en caso de ser así levanta una exepción y muestra un codigo 4xx)
            throw new ApplicationException(
                ErrorCase.ALREADY_EXISTS,
                    "Ya existe un usuario con este email"
            );
        } //si eso no se levanta entonces se crea
        // lo paso a entiedad con ayuda del maper
        User user = userMapper.toEntity(dto); // este aun no tiene id
        User savedUser = userRepository.save(user); // ey repo, haz el metodo save(si no existe lo crea si ya existe lo actualiza)
        return userMapper.toResponseDTO(savedUser); // convierta el objeto en fichitas, pero le quita la contraseña para mostrarselo al usuario
    }

    // Listar usuarios
    public List<UserResponseDTO> findAll() {
        List<User> users =userRepository.findAll(); // obtenemos una lista de usuarios - findAll viene de la herencia
        return userMapper.toResponseDTOList(users); // manda la lista sin contraseñas
    }

    // Listar un solo usuario (recibe el identificador) -**aca hay un problema, pensarlo
    private User findUser(Long id) { //un metodo private solo se puede usar en esta clase, alguien de afuera no lo ve
        return userRepository.findById(id)
            .orElseThrow(() -> new ApplicationException(
                ErrorCase.NOT_FOUND,
                "Usuario no encontrado con id: " + id
                ));
    }

    /*endpoint de listar un unico usuario con su perfil (metodo en el servicio que me
     trae un unico usuario con su perfil)*/
    public UserDetailResponseDTO findByIdAndProfile(Long id) {//recibe el identificador de un usuario
        User user =userRepository//el repositorio se encarga de traer al usuario
                .findWithProfileById(id)//hace un join internamente
                .orElseThrow(//si sale mal lanza una excepción
                        () -> new ApplicationException(
                                ErrorCase.NOT_FOUND,
                                "Usuario no encontrado con id: " + id
                        )
                );

        return userMapper.toDetailResponseDTO(user);//si todo esta okey retorna usuario con perfil
    }

    public UserSessionsResponseDTO findByIdAndSessions(Long id) {
        User user = userRepository.findWithSessionsById(id)
                .orElseThrow(() -> new ApplicationException(
                        ErrorCase.NOT_FOUND,
                        "Usuario no encontrado con id: " + id
                ));

        return userMapper.toSessionsResponseDTO(user);
    }

    public UserResponseDTO findById(Long id) {
        User user = findUser(id);
        return userMapper.toResponseDTO(user);
    }

    public UserResponseDTO update(Long id, UpdateUserDTO dto) {
        User user = findUser(id);// usuario actual en la base de datos
        // verifica si ese correo ya le pertenece a otro usuario
        if (userRepository.existsByEmailAndIdNot(dto.getEmail(), id)) {
                throw new ApplicationException(
                    ErrorCase.ALREADY_EXISTS,
                    "El email pertenece a otro usuario"
            );
        }
        // con los datos que llegaron en el dto lo convierto a usuario
        userMapper.updateEntity(dto, user); // este user es el ya se esta actualizando, aca internamente el objeto ya se cambio por eso no hay que retornarlo
        User updatedUser = userRepository.save(user);
        return userMapper.toResponseDTO(updatedUser);
    }
    public void delete(Long id) {
        User user = findUser(id); //busco el elemento a eliminar
        userRepository.delete(user);//le digo al repo que lo elimine
    }
}

/*
* Una consulta SQL no puede quedar acá, eso es responsabilidad del repositorio
* Caso hipotetico: Saber que usuario tiene el correo con mayor longitud. Por lo general se programaría sen el servicio, pero esa responsabilidad
* seria más de la base de datos, ya que la base de datos aplique el algoritmo, si se le pude asignar algo a la base de datos, es mejor que lo
* haga la base de datos, pero lo ideal es no estar yendo y volviendo de la base de datos, no hacer tantas consultas o inserciones ahí, es decir
* no llamar repositorios dentro de un ciclo, mejor hacerlo al final del ciclo, todo en una sola ida*/

/*En la vida real no se eliminan registros, uno solo los oculta*/

/*Los joins son muy rapidos por naturaleza en bases de datos, por eso el back carga una vez info que quizas luego se necesite de nuevo
REDIS INTRODUCTION, es como una cache (pero esta cargado en ram)*/