package com.uc.ms_security.service;

import com.uc.ms_security.dto.permission.PermissionRequestDTO;
import com.uc.ms_security.entity.Permission;
import com.uc.ms_security.exception.ApplicationException;
import com.uc.ms_security.exception.ErrorCase;
import com.uc.ms_security.mapper.PermissionMapper;
import com.uc.ms_security.repository.PermissionRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    public Permission create(@Valid PermissionRequestDTO dto) {
        return permissionRepository.save(permissionMapper.toEntity(dto));
    }

    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    public Permission findById(Long id) {
        return findPermission(id);
    }

    public Permission update(Long id, @Valid PermissionRequestDTO dto) {
        Permission permission = findPermission(id);
        permissionMapper.updateEntity(dto, permission);
        return permissionRepository.save(permission);
    }

    public void delete(Long id) {
        permissionRepository.delete(findPermission(id));
    }

    private Permission findPermission(Long id) {
        return permissionRepository.findById(id).orElseThrow(() -> new ApplicationException(
                ErrorCase.NOT_FOUND, "Permiso no encontrado con id: " + id));
    }
}