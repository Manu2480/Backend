package com.uc.ms_security.controller;

import com.uc.ms_security.dto.user.CreateUserDTO;
import com.uc.ms_security.dto.user.UpdateUserDTO;
import com.uc.ms_security.dto.user.UserDetailResponseDTO;
import com.uc.ms_security.dto.user.UserResponseDTO;
import com.uc.ms_security.dto.user.UserSessionsResponseDTO;
import com.uc.ms_security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")//definimos como seran las rutas para definirse desde el cliente
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;//inyección es decir el controlador necesita del servicio

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO create(@Valid @RequestBody CreateUserDTO dto) {//en creación se envia todo completo
        return userService.create(dto);//si todo sale bien solicitamos la creación y enviamos el dto
    }

    @GetMapping
    public List<UserResponseDTO> findAll() {//listar todos los usuarios
        return userService.findAll();
    }


    @GetMapping("/{id}")
    public UserResponseDTO findById(@PathVariable Long id) {//listar uno solo
        return userService.findById(id);//le digo al servicio que lo busque
    }

    @PutMapping("/{id}") //recibe la variable
    public UserResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserDTO dto) {
        return userService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    @GetMapping("/{id}/detail")
    public UserDetailResponseDTO findByIdAndProfile(
            @PathVariable Long id) {
        return userService.findByIdAndProfile(id);
    }

    @GetMapping("/{id}/detail-with-sessions")
    public UserSessionsResponseDTO findByIdAndSessions(
            @PathVariable Long id) {
        return userService.findByIdAndSessions(id);
    }
}