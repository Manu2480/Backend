package com.uc.ms_security;

import com.uc.ms_security.dto.role.RoleRequestDTO;
import com.uc.ms_security.dto.role.RoleResponseDTO;
import com.uc.ms_security.dto.user.UserRolesResponseDTO;
import com.uc.ms_security.dto.userRole.RoleUserResponseDTO;
import com.uc.ms_security.dto.userRole.AssignRoleRequestDTO;
import com.uc.ms_security.dto.userRole.UserRoleResponseDTO;
import com.uc.ms_security.entity.User;
import com.uc.ms_security.exception.ApplicationException;
import com.uc.ms_security.exception.ErrorCase;
import com.uc.ms_security.repository.UserRepository;
import com.uc.ms_security.service.RoleService;
import com.uc.ms_security.service.UserRoleService;
import com.uc.ms_security.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "security.jwt.secret=test-only-jwt-secret-with-at-least-32-bytes")
@Transactional
class UserRoleRelationshipTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private UserService userService;

    @Test
    void managesManyToManyAssignmentsWithoutDuplicatesOrRecursiveResponses() {
        User user = createUser();
        User userWithoutRoles = createUser();
        String suffix = UUID.randomUUID().toString();
        RoleResponseDTO role = roleService.create(createRole("EDITOR-" + suffix));
        RoleResponseDTO secondUserRole = roleService.create(createRole("AUDITOR-" + suffix));

        AssignRoleRequestDTO assignment = new AssignRoleRequestDTO();
        assignment.setUserId(user.getId());
        assignment.setRoleId(role.getId());

        UserRoleResponseDTO created = userRoleService.assign(assignment);
        assertEquals(role.getId(), created.getRole().getId());
        assertEquals(role.getName(), created.getRole().getName());

        AssignRoleRequestDTO secondAssignment = new AssignRoleRequestDTO();
        secondAssignment.setUserId(user.getId());
        secondAssignment.setRoleId(secondUserRole.getId());
        userRoleService.assign(secondAssignment);

        UserRolesResponseDTO userWithRoles = userService.findByIdAndRoles(user.getId());
        assertEquals(2, userWithRoles.getRoles().size());
        assertTrue(userWithRoles.getRoles().stream()
                .anyMatch(userRole -> role.getId().equals(userRole.getRole().getId())));
        assertTrue(userService.findByIdAndRoles(userWithoutRoles.getId())
                .getRoles().isEmpty());

        RoleUserResponseDTO userWithRole = userRoleService.findByRoleId(role.getId()).get(0);
        assertEquals(user.getId(), userWithRole.getUser().getId());

        userRoleService.delete(user.getId(), role.getId());
        assertTrue(userRoleService.findByUserId(user.getId()).stream()
                .noneMatch(userRole -> role.getId().equals(userRole.getRole().getId())));
        assertEquals(role.getId(), roleService.findById(role.getId()).getId());
        roleService.delete(role.getId());
        assertEquals(secondUserRole.getId(), roleService.findById(secondUserRole.getId()).getId());
    }

    @Test
    void rejectsDuplicateRoleNamesIgnoringCaseAndWhitespace() {
        String name = "EDITOR-" + UUID.randomUUID();
        roleService.create(createRole(name));

        ApplicationException duplicateRoleName = assertThrows(
                ApplicationException.class,
                () -> roleService.create(createRole(" " + name.toLowerCase() + " "))
        );
        assertEquals(ErrorCase.ALREADY_EXISTS, duplicateRoleName.getErrorCase());
    }

    @Test
    void rejectsDuplicateAssignments() {
        User user = createUser();
        RoleResponseDTO role = roleService.create(
                createRole("EDITOR-" + UUID.randomUUID())
        );
        AssignRoleRequestDTO assignment = new AssignRoleRequestDTO();
        assignment.setUserId(user.getId());
        assignment.setRoleId(role.getId());
        userRoleService.assign(assignment);

        ApplicationException duplicateAssignment = assertThrows(
                ApplicationException.class,
                () -> userRoleService.assign(assignment)
        );
        assertEquals(ErrorCase.ALREADY_EXISTS, duplicateAssignment.getErrorCase());
    }

    @Test
    void preventsDeletingARoleThatIsAssigned() {
        User user = createUser();
        RoleResponseDTO role = roleService.create(
                createRole("ADMIN-" + UUID.randomUUID())
        );
        AssignRoleRequestDTO assignment = new AssignRoleRequestDTO();
        assignment.setUserId(user.getId());
        assignment.setRoleId(role.getId());
        userRoleService.assign(assignment);

        ApplicationException assignedRoleDelete = assertThrows(
                ApplicationException.class,
                () -> roleService.delete(role.getId())
        );
        assertEquals(ErrorCase.INVALID_OPERATION, assignedRoleDelete.getErrorCase());
    }

    private User createUser() {
        User user = new User();
        user.setName("Role test");
        user.setEmail(UUID.randomUUID() + "@example.test");
        user.setPassword("test-password");
        return userRepository.save(user);
    }

    private RoleRequestDTO createRole(String name) {
        RoleRequestDTO role = new RoleRequestDTO();
        role.setName(name);
        role.setDescription("Role used in relationship tests");
        return role;
    }
}
