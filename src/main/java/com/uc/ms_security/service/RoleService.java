package com.uc.ms_security.service;

import com.uc.ms_security.dto.role.RoleRequestDTO;
import com.uc.ms_security.dto.role.RoleResponseDTO;
import com.uc.ms_security.entity.Role;
import com.uc.ms_security.exception.ApplicationException;
import com.uc.ms_security.exception.ErrorCase;
import com.uc.ms_security.mapper.RoleMapper;
import com.uc.ms_security.repository.RolePermissionRepository;
import com.uc.ms_security.repository.RoleRepository;
import com.uc.ms_security.repository.UserRoleRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final RoleMapper roleMapper;

    @Transactional
    public RoleResponseDTO create(@Valid RoleRequestDTO dto) {
        String name = dto.getName().trim();
        if (roleRepository.existsByNameIgnoreCase(name)) {
            throw new ApplicationException(
                    ErrorCase.ALREADY_EXISTS,
                    "Ya existe un rol con ese nombre"
            );
        }

        return roleMapper.toResponseDTO(
                roleRepository.save(roleMapper.toEntity(dto))
        );
    }

    @Transactional(readOnly = true)
    public List<RoleResponseDTO> findAll() {
        return roleMapper.toResponseDTOList(roleRepository.findAll());
    }

    @Transactional(readOnly = true)
    public RoleResponseDTO findById(Long id) {
        return roleMapper.toResponseDTO(findRole(id));
    }

    @Transactional
    public RoleResponseDTO update(Long id, @Valid RoleRequestDTO dto) {
        Role role = findRole(id);

        if (roleRepository.existsByNameIgnoreCaseAndIdNot(
                dto.getName().trim(),
                id
        )) {
            throw new ApplicationException(
                    ErrorCase.ALREADY_EXISTS,
                    "Ya existe un rol con ese nombre"
            );
        }

        roleMapper.updateEntity(dto, role);
        return roleMapper.toResponseDTO(roleRepository.save(role));
    }

    @Transactional
    public void delete(Long id) {
        Role role = findRole(id);

        if (userRoleRepository.existsByRoleId(id)
                || rolePermissionRepository.existsByRoleId(id)) {
            throw new ApplicationException(
                    ErrorCase.INVALID_OPERATION,
                    "No se puede eliminar un rol que tiene asignaciones o permisos asociados"
            );
        }

        roleRepository.delete(role);
    }

    private Role findRole(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new ApplicationException(
                ErrorCase.NOT_FOUND, "Rol no encontrado con id: " + id));
    }
}
