package com.uc.ms_security;

import com.uc.ms_security.dto.session.SessionRequestDTO;
import com.uc.ms_security.dto.session.SessionResponseDTO;
import com.uc.ms_security.dto.user.UserSessionsResponseDTO;
import com.uc.ms_security.entity.User;
import com.uc.ms_security.exception.ApplicationException;
import com.uc.ms_security.exception.ErrorCase;
import com.uc.ms_security.repository.UserRepository;
import com.uc.ms_security.service.SessionService;
import com.uc.ms_security.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class SessionRelationshipTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserService userService;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void returnsUsersSessionsAndRejectsSessionsOwnedByAnotherUser() {
        User owner = createUser();
        User anotherUser = createUser();

        SessionRequestDTO request = new SessionRequestDTO();
        request.setToken(UUID.randomUUID().toString());
        request.setExpiration(LocalDateTime.now().plusHours(1));
        request.setCode2FA("238194");

        SessionResponseDTO created = sessionService.create(owner.getId(), request);
        entityManager.flush();
        entityManager.clear();
        UserSessionsResponseDTO userWithSessions =
                userService.findByIdAndSessions(owner.getId());

        assertEquals(created.getId(), userWithSessions.getSessions().get(0).getId());
        assertEquals(request.getToken(), userWithSessions.getSessions().get(0).getToken());
        assertEquals(request.getCode2FA(), userWithSessions.getSessions().get(0).getCode2FA());
        assertEquals(
                created.getId(),
                sessionService.findById(owner.getId(), created.getId()).getId()
        );

        ApplicationException exception = assertThrows(
                ApplicationException.class,
                () -> sessionService.findById(anotherUser.getId(), created.getId())
        );
        assertEquals(ErrorCase.NOT_FOUND, exception.getErrorCase());
    }

    private User createUser() {
        User user = new User();
        user.setName("Session test");
        user.setEmail(UUID.randomUUID() + "@example.test");
        user.setPassword("test-password");
        return userRepository.save(user);
    }
}
