package com.uc.ms_security.service;

import com.uc.ms_security.dto.role.RoleRequestDTO;
import com.uc.ms_security.entity.Role;
import com.uc.ms_security.exception.ApplicationException;
import com.uc.ms_security.exception.ErrorCase;
import com.uc.ms_security.mapper.RoleMapper;
import com.uc.ms_security.repository.RoleRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    public Role create(@Valid RoleRequestDTO dto) {
        return roleRepository.save(roleMapper.toEntity(dto));
    }

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role findById(Long id) {
        return findRole(id);
    }

    public Role update(Long id, @Valid RoleRequestDTO dto) {
        Role role = findRole(id);
        roleMapper.updateEntity(dto, role);
        return roleRepository.save(role);
    }

    public void delete(Long id) {
        roleRepository.delete(findRole(id));
    }

    private Role findRole(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new ApplicationException(
                ErrorCase.NOT_FOUND, "Rol no encontrado con id: " + id));
    }
}