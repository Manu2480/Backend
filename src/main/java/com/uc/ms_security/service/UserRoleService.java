package com.uc.ms_security.service;

import com.uc.ms_security.dto.userRole.AssignRoleRequestDTO;
import com.uc.ms_security.dto.userRole.RoleUserResponseDTO;
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
import org.springframework.transaction.annotation.Transactional;
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

    //hace que se deban cumplir todas las caracteristicas de la 
    // función, para hacer la transacción en la base de datos
    @Transactional 
    public UserRoleResponseDTO assign(@Valid AssignRoleRequestDTO dto) {
        User user = findUser(dto.getUserId());
        Role role = findRole(dto.getRoleId());

        if (userRoleRepository.existsByUserIdAndRoleId(
                dto.getUserId(),
                dto.getRoleId()
        )) {
            throw new ApplicationException(
                    ErrorCase.ALREADY_EXISTS,
                    "El usuario ya tiene asignado ese rol"
            );
        }

        UserRole userRole = new UserRole();
        user.addUserRole(userRole);
        role.addUserRole(userRole);
        return userRoleMapper.toResponseDTO(userRoleRepository.save(userRole));
    }

    @Transactional(readOnly = true)
    public List<UserRoleResponseDTO> findAll() {
        return userRoleMapper.toResponseDTOList(userRoleRepository.findAll());
    }

    @Transactional(readOnly = true)
    public List<UserRoleResponseDTO> findByUserId(Long userId) {
        findUser(userId);
        return userRoleMapper.toResponseDTOList(
                userRoleRepository.findAllByUserId(userId)
        );
    }

    @Transactional(readOnly = true)
    public List<RoleUserResponseDTO> findByRoleId(Long roleId) {
        findRole(roleId);
        return userRoleMapper.toRoleUserResponseDTOList(
                userRoleRepository.findAllByRoleId(roleId)
        );
    }

    @Transactional
    public void delete(Long userId, Long roleId) {
        UserRole userRole = userRoleRepository.findByUserIdAndRoleId(userId, roleId)
                .orElseThrow(() -> new ApplicationException(
                        ErrorCase.NOT_FOUND,
                        "Asignación de rol no encontrada"
                ));
        userRole.getUser().removeUserRole(userRole);
        userRole.getRole().removeUserRole(userRole);
        userRoleRepository.delete(userRole);
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
