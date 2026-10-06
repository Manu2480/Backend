package com.uc.ms_security.mapper;

import com.uc.ms_security.dto.userRole.UserRoleResponseDTO;
import com.uc.ms_security.entity.Role;
import com.uc.ms_security.entity.User;
import com.uc.ms_security.entity.UserRole;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRoleMapper {

    public UserRole toEntity(User user, Role role) {
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        return userRole;
    }

    public void updateEntity(UserRole userRole, User user, Role role) {
        userRole.setUser(user);
        userRole.setRole(role);
    }

    public UserRoleResponseDTO toResponseDTO(UserRole userRole) {
        return new UserRoleResponseDTO(
                userRole.getId(),
                userRole.getUser().getId(),
                userRole.getRole().getId()
        );
    }

    public List<UserRoleResponseDTO> toResponseDTOList(List<UserRole> userRoles) {
        return userRoles.stream().map(this::toResponseDTO).toList();
    }
}