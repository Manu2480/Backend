package com.uc.ms_security.controller;

import com.uc.ms_security.dto.userRole.UserRoleRequestDTO;
import com.uc.ms_security.dto.userRole.UserRoleResponseDTO;
import com.uc.ms_security.service.UserRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-roles")
@RequiredArgsConstructor
public class UserRoleController {

    private final UserRoleService userRoleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserRoleResponseDTO create(@Valid @RequestBody UserRoleRequestDTO dto) {
        return userRoleService.create(dto);
    }

    @GetMapping
    public List<UserRoleResponseDTO> findAll() {
        return userRoleService.findAll();
    }

    @GetMapping("/{id}")
    public UserRoleResponseDTO findById(@PathVariable Long id) {
        return userRoleService.findById(id);
    }

    @PutMapping("/{id}")
    public UserRoleResponseDTO update(@PathVariable Long id, @Valid @RequestBody UserRoleRequestDTO dto) {
        return userRoleService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userRoleService.delete(id);
    }
}