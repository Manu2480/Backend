package com.uc.ms_security.controller;

import com.uc.ms_security.dto.permission.PermissionRequestDTO;
import com.uc.ms_security.entity.Permission;
import com.uc.ms_security.service.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Permission create(@Valid @RequestBody PermissionRequestDTO dto) {
        return permissionService.create(dto);
    }

    @GetMapping
    public List<Permission> findAll() {
        return permissionService.findAll();
    }

    @GetMapping("/{id}")
    public Permission findById(@PathVariable Long id) {
        return permissionService.findById(id);
    }

    @PutMapping("/{id}")
    public Permission update(@PathVariable Long id, @Valid @RequestBody PermissionRequestDTO dto) {
        return permissionService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        permissionService.delete(id);
    }
}