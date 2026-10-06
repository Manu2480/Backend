package com.uc.ms_security.service;

import com.uc.ms_security.dto.rolePermission.RolePermissionRequestDTO;
import com.uc.ms_security.entity.Permission;
import com.uc.ms_security.entity.Role;
import com.uc.ms_security.entity.RolePermission;
import com.uc.ms_security.exception.ApplicationException;
import com.uc.ms_security.exception.ErrorCase;
import com.uc.ms_security.mapper.RolePermissionMapper;
import com.uc.ms_security.repository.PermissionRepository;
import com.uc.ms_security.repository.RolePermissionRepository;
import com.uc.ms_security.repository.RoleRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class RolePermissionService {

    private final RolePermissionRepository rolePermissionRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionMapper rolePermissionMapper;

    public RolePermission create(@Valid RolePermissionRequestDTO dto) {
        Role role = findRole(dto.getRoleId());
        Permission permission = findPermission(dto.getPermissionId());
        return rolePermissionRepository.save(rolePermissionMapper.toEntity(role, permission));
    }

    public List<RolePermission> findAll() {
        return rolePermissionRepository.findAll();
    }

    public RolePermission findById(Long id) {
        return findRolePermission(id);
    }

    public RolePermission update(Long id, @Valid RolePermissionRequestDTO dto) {
        RolePermission rolePermission = findRolePermission(id);
        Role role = findRole(dto.getRoleId());
        Permission permission = findPermission(dto.getPermissionId());
        rolePermissionMapper.updateEntity(rolePermission, role, permission);
        return rolePermissionRepository.save(rolePermission);
    }

    public void delete(Long id) {
        rolePermissionRepository.delete(findRolePermission(id));
    }

    private RolePermission findRolePermission(Long id) {
        return rolePermissionRepository.findById(id).orElseThrow(() -> new ApplicationException(
                ErrorCase.NOT_FOUND, "Asignación de permiso no encontrada con id: " + id));
    }

    private Role findRole(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new ApplicationException(
                ErrorCase.NOT_FOUND, "Rol no encontrado con id: " + id));
    }

    private Permission findPermission(Long id) {
        return permissionRepository.findById(id).orElseThrow(() -> new ApplicationException(
                ErrorCase.NOT_FOUND, "Permiso no encontrado con id: " + id));
    }
}