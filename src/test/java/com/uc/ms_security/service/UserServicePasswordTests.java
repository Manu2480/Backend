package com.uc.ms_security.service;

import com.uc.ms_security.dto.user.CreateUserDTO;
import com.uc.ms_security.dto.user.UpdateUserDTO;
import com.uc.ms_security.dto.user.UserResponseDTO;
import com.uc.ms_security.entity.User;
import com.uc.ms_security.mapper.UserMapper;
import com.uc.ms_security.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class UserServicePasswordTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void createStoresEncodedPasswordAndReturnsPasswordFreeResponse() {
        CreateUserDTO request = createRequest();
        User user = new User();
        UserResponseDTO response = new UserResponseDTO(1L, "Ana Gómez", "ana@example.com");

        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("ClaveSegura123!")).thenReturn("$2a$12$encoded");
        when(userMapper.toEntity(request, "$2a$12$encoded")).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(response);

        UserResponseDTO actual = userService.create(request);

        assertEquals(response, actual);
        verify(userMapper).toEntity(request, "$2a$12$encoded");
        verify(userRepository).save(user);
    }

    @Test
    void updateWithoutPasswordKeepsExistingHash() {
        User user = new User();
        user.setPassword("$2a$12$existing");
        UpdateUserDTO request = updateRequest(null);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmailAndIdNot(request.getEmail(), 1L)).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDTO(user))
                .thenReturn(new UserResponseDTO(1L, "Ana Gómez", "ana@example.com"));

        userService.update(1L, request);

        assertEquals("$2a$12$existing", user.getPassword());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void updateWithNewPasswordStoresNewHash() {
        User user = new User();
        user.setPassword("$2a$12$existing");
        UpdateUserDTO request = updateRequest("NuevaClave123!");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByEmailAndIdNot(request.getEmail(), 1L)).thenReturn(false);
        when(passwordEncoder.encode("NuevaClave123!")).thenReturn("$2a$12$new-hash");
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDTO(user))
                .thenReturn(new UserResponseDTO(1L, "Ana Gómez", "ana@example.com"));

        userService.update(1L, request);

        assertEquals("$2a$12$new-hash", user.getPassword());
        verify(passwordEncoder).encode("NuevaClave123!");
    }

    private CreateUserDTO createRequest() {
        CreateUserDTO request = new CreateUserDTO();
        request.setName("Ana Gómez");
        request.setEmail("ana@example.com");
        request.setPassword("ClaveSegura123!");
        return request;
    }

    private UpdateUserDTO updateRequest(String password) {
        UpdateUserDTO request = new UpdateUserDTO();
        request.setName("Ana Gómez");
        request.setEmail("ana@example.com");
        request.setPassword(password);
        return request;
    }
}
