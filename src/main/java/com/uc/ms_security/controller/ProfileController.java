package com.uc.ms_security.controller;

import com.uc.ms_security.dto.profile.ProfileRequestDTO;
import com.uc.ms_security.dto.profile.ProfileResponseDTO;
import com.uc.ms_security.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    @PostMapping("/users/{userId}/profile")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponseDTO create(
            @PathVariable Long userId,
            @Valid @RequestBody ProfileRequestDTO dto) {
        return profileService.create(userId, dto);
    }

    @GetMapping("/profiles")
    public List<ProfileResponseDTO> findAll() {
        return profileService.findAll();
    }

    @GetMapping("/users/{userId}/profile")
    public ProfileResponseDTO findByUserId(@PathVariable Long userId) {
        return profileService.findByUserId(userId);
    }

    @PutMapping("/users/{userId}/profile")
    public ProfileResponseDTO update(@PathVariable Long userId, @Valid @RequestBody ProfileRequestDTO dto) {
        return profileService.update(userId, dto);
    }

    @DeleteMapping("/users/{userId}/profile")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId) {
        profileService.delete(userId);
    }
}