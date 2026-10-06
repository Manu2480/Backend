package com.uc.ms_security.mapper;

import com.uc.ms_security.dto.profile.ProfileRequestDTO;
import com.uc.ms_security.dto.profile.ProfileResponseDTO;
import com.uc.ms_security.entity.Profile;
import com.uc.ms_security.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProfileMapper {

    public Profile toEntity(ProfileRequestDTO dto, User user) {
        Profile profile = new Profile();
        profile.setPhone(dto.getPhone());
        profile.setBirthDate(dto.getBirthDate());
        profile.setUser(user);
        return profile;
    }

    public void updateEntity(ProfileRequestDTO dto, Profile profile) {
        profile.setPhone(dto.getPhone());
        profile.setBirthDate(dto.getBirthDate());
    }

    public ProfileResponseDTO toResponseDTO(Profile profile) {

        //si perfil es nulo, retorne nulo
        if (profile == null) {
            return null;
        }

        return new ProfileResponseDTO(
                profile.getId(),
                profile.getPhone(),
                profile.getBirthDate(),
                profile.getUser() == null ? null : profile.getUser().getId()
        );
    }

    public List<ProfileResponseDTO> toResponseDTOList(List<Profile> profiles) {
        return profiles.stream().map(this::toResponseDTO).toList();
    }
}