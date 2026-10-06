package com.uc.ms_security.controller;

import com.uc.ms_security.dto.rolePermission.RolePermissionRequestDTO;
import com.uc.ms_security.entity.RolePermission;
import com.uc.ms_security.service.RolePermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/role-permissions")
@RequiredArgsConstructor
public class RolePermissionController {

    private final RolePermissionService rolePermissionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RolePermission create(@Valid @RequestBody RolePermissionRequestDTO dto) {
        return rolePermissionService.create(dto);
    }

    @GetMapping
    public List<RolePermission> findAll() {
        return rolePermissionService.findAll();
    }

    @GetMapping("/{id}")
    public RolePermission findById(@PathVariable Long id) {
        return rolePermissionService.findById(id);
    }

    @PutMapping("/{id}")
    public RolePermission update(@PathVariable Long id, @Valid @RequestBody RolePermissionRequestDTO dto) {
        return rolePermissionService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        rolePermissionService.delete(id);
    }
}