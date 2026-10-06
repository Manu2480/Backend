package com.uc.ms_security.repository;

import com.uc.ms_security.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    /*Si fuera una base no relacional, estaran sus propias consultas y se pondrían acá*/

    /*Este metodo busca un perfil dado el id del usuario, es diferente
    a buscar un perfil pbyid es decir por id del perfil no del usuario*/
    Optional<Profile> findByUserId(Long userId);

    /*devuelve un booleano que verificando si existe un perfil dado
    el identificador de un usuario*/
    boolean existsByUserId(Long userId);
}