package com.uc.ms_security.service;

import com.uc.ms_security.dto.userRole.UserRoleRequestDTO;
import com.uc.ms_security.dto.userRole.UserRoleResponseDTO;
import com.uc.ms_security.entity.Role;
import com.uc.ms_security.entity.User;
import com.uc.ms_security.entity.UserRole;
import com.uc.ms_security.exception.ApplicationException;
import com.uc.ms_security.exception.ErrorCase;
import com.uc.ms_security.mapper.UserRoleMapper;
import com.uc.ms_security.repository.RoleRepository;
import com.uc.ms_security.repository.UserRepository;
import com.uc.ms_security.repository.UserRoleRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleMapper userRoleMapper;

    public UserRoleResponseDTO create(@Valid UserRoleRequestDTO dto) {
        User user = findUser(dto.getUserId());
        Role role = findRole(dto.getRoleId());
        return userRoleMapper.toResponseDTO(userRoleRepository.save(userRoleMapper.toEntity(user, role)));
    }

    public List<UserRoleResponseDTO> findAll() {
        return userRoleMapper.toResponseDTOList(userRoleRepository.findAll());
    }

    public UserRoleResponseDTO findById(Long id) {
        return userRoleMapper.toResponseDTO(findUserRole(id));
    }

    public UserRoleResponseDTO update(Long id, @Valid UserRoleRequestDTO dto) {
        UserRole userRole = findUserRole(id);
        User user = findUser(dto.getUserId());
        Role role = findRole(dto.getRoleId());
        userRoleMapper.updateEntity(userRole, user, role);
        return userRoleMapper.toResponseDTO(userRoleRepository.save(userRole));
    }

    public void delete(Long id) {
        userRoleRepository.delete(findUserRole(id));
    }

    private UserRole findUserRole(Long id) {
        return userRoleRepository.findById(id).orElseThrow(() -> new ApplicationException(
                ErrorCase.NOT_FOUND, "Asignación de rol no encontrada con id: " + id));
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ApplicationException(
                ErrorCase.NOT_FOUND, "Usuario no encontrado con id: " + id));
    }

    private Role findRole(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new ApplicationException(
                ErrorCase.NOT_FOUND, "Rol no encontrado con id: " + id));
    }
}