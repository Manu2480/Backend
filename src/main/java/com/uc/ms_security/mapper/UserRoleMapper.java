package com.uc.ms_security.mapper;

import com.uc.ms_security.dto.user.UserResponseDTO;
import com.uc.ms_security.dto.userRole.RoleUserResponseDTO;
import com.uc.ms_security.dto.userRole.UserRoleResponseDTO;
import com.uc.ms_security.entity.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserRoleMapper {

    private final RoleMapper roleMapper;

    public UserRoleResponseDTO toResponseDTO(UserRole userRole) {
        return new UserRoleResponseDTO(
                userRole.getId(),
                userRole.getUser().getId(),
                roleMapper.toResponseDTO(userRole.getRole())
        );
    }

    public List<UserRoleResponseDTO> toResponseDTOList(List<UserRole> userRoles) {
        return userRoles.stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public RoleUserResponseDTO toRoleUserResponseDTO(UserRole userRole) {
        return new RoleUserResponseDTO(
                userRole.getId(),
                userRole.getRole().getId(),
                new UserResponseDTO(
                        userRole.getUser().getId(),
                        userRole.getUser().getName(),
                        userRole.getUser().getEmail()
                )
        );
    }

    public List<RoleUserResponseDTO> toRoleUserResponseDTOList(
            List<UserRole> userRoles) {
        return userRoles.stream()
                .map(this::toRoleUserResponseDTO)
                .toList();
    }
}