package com.uc.ms_security.service;

import com.uc.ms_security.dto.auth.LoginRequestDTO;
import com.uc.ms_security.dto.auth.AuthResponseDTO;
import com.uc.ms_security.entity.User;
import com.uc.ms_security.exception.ApplicationException;
import com.uc.ms_security.exception.ErrorCase;
import com.uc.ms_security.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Test
    void loginWithValidCredentialsIssuesToken() {
        LoginRequestDTO request = loginRequest();
        User user = new User();
        AuthResponseDTO response = new AuthResponseDTO(
                "signed-token",
                "Bearer",
                Instant.now().plusSeconds(900)
        );

        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn(response);

        assertEquals(response, authService.login(request));
        verify(jwtService).generateToken(user);
    }

    @Test
    void loginRejectsUnknownEmailWithoutRevealingIt() {
        when(userRepository.findByEmail("ana@example.com"))
                .thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(
                ApplicationException.class,
                () -> authService.login(loginRequest())
        );

        assertEquals(ErrorCase.UNAUTHORIZED, exception.getErrorCase());
        assertEquals("Credenciales incorrectas", exception.getMessage());
        verifyNoInteractions(passwordEncoder, jwtService);
    }

    @Test
    void loginRejectsIncorrectPasswordWithGenericMessage() {
        User user = new User();
        when(userRepository.findByEmail("ana@example.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("ClaveIncorrecta123!", user.getPassword()))
                .thenReturn(false);

        ApplicationException exception = assertThrows(
                ApplicationException.class,
                () -> authService.login(loginRequest())
        );

        assertEquals(ErrorCase.UNAUTHORIZED, exception.getErrorCase());
        assertEquals("Credenciales incorrectas", exception.getMessage());
        verifyNoInteractions(jwtService);
    }

    private LoginRequestDTO loginRequest() {
        LoginRequestDTO request = new LoginRequestDTO();
        request.setEmail("ana@example.com");
        request.setPassword("ClaveIncorrecta123!");
        return request;
    }
}
