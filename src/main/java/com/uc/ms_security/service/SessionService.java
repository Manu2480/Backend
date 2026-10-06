package com.uc.ms_security.service;

import com.uc.ms_security.dto.session.SessionRequestDTO;
import com.uc.ms_security.dto.session.SessionResponseDTO;
import com.uc.ms_security.entity.Session;
import com.uc.ms_security.entity.User;
import com.uc.ms_security.exception.ApplicationException;
import com.uc.ms_security.exception.ErrorCase;
import com.uc.ms_security.mapper.SessionMapper;
import com.uc.ms_security.repository.SessionRepository;
import com.uc.ms_security.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final SessionMapper sessionMapper;

    public SessionResponseDTO create(Long userId, @Valid SessionRequestDTO dto) {
        return createForUser(findUser(userId), dto);
    }

    public List<SessionResponseDTO> findAllByUserId(Long userId) {
        findUser(userId);
        return sessionMapper.toResponseDTOList(
                sessionRepository.findAllByUserId(userId)
        );
    }

    public SessionResponseDTO findById(Long userId, Long sessionId) {
        return sessionMapper.toResponseDTO(findSession(userId, sessionId));
    }

    public SessionResponseDTO update(
            Long userId,
            Long sessionId,
            @Valid SessionRequestDTO dto) {

        User user = findUser(userId);
        return updateSession(findSession(userId, sessionId), dto, user);
    }

    public void delete(Long userId, Long sessionId) {
        sessionRepository.delete(findSession(userId, sessionId));
    }

    private SessionResponseDTO createForUser(
            User user,
            SessionRequestDTO dto) {

        ensureTokenAvailable(dto.getToken(), null);
        Session session = sessionMapper.toEntity(dto, user);
        return sessionMapper.toResponseDTO(sessionRepository.save(session));
    }

    private SessionResponseDTO updateSession(
            Session session,
            SessionRequestDTO dto,
            User user) {

        ensureTokenAvailable(dto.getToken(), session.getId());
        sessionMapper.updateEntity(dto, session, user);
        return sessionMapper.toResponseDTO(sessionRepository.save(session));
    }

    private void ensureTokenAvailable(String token, Long sessionId) {
        boolean alreadyExists = sessionId == null
                ? sessionRepository.existsByToken(token)
                : sessionRepository.existsByTokenAndIdNot(token, sessionId);

        if (alreadyExists) {
            throw new ApplicationException(
                    ErrorCase.ALREADY_EXISTS,
                    "El token ya está registrado"
            );
        }
    }

    private Session findSession(Long userId, Long sessionId) {
        return sessionRepository.findByIdAndUserId(sessionId, userId)
                .orElseThrow(() -> new ApplicationException(
                        ErrorCase.NOT_FOUND,
                        "Sesión no encontrada para este usuario"
                ));
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ApplicationException(
                ErrorCase.NOT_FOUND, "Usuario no encontrado con id: " + id));
    }
}