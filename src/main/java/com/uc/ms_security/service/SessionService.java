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

    public SessionResponseDTO create(@Valid SessionRequestDTO dto) {
        User user = findUser(dto.getUserId());
        return sessionMapper.toResponseDTO(sessionRepository.save(sessionMapper.toEntity(dto, user)));
    }

    public List<SessionResponseDTO> findAll() {
        return sessionMapper.toResponseDTOList(sessionRepository.findAll());
    }

    public SessionResponseDTO findById(Long id) {
        return sessionMapper.toResponseDTO(findSession(id));
    }

    public SessionResponseDTO update(Long id, @Valid SessionRequestDTO dto) {
        Session session = findSession(id);
        User user = findUser(dto.getUserId());
        sessionMapper.updateEntity(dto, session, user);
        return sessionMapper.toResponseDTO(sessionRepository.save(session));
    }

    public void delete(Long id) {
        sessionRepository.delete(findSession(id));
    }

    private Session findSession(Long id) {
        return sessionRepository.findById(id).orElseThrow(() -> new ApplicationException(
                ErrorCase.NOT_FOUND, "Sesión no encontrada con id: " + id));
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ApplicationException(
                ErrorCase.NOT_FOUND, "Usuario no encontrado con id: " + id));
    }
}