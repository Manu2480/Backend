package com.uc.ms_security.repository;

import com.uc.ms_security.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    //si un rol ya existe
    boolean existsByNameIgnoreCase(String name);

    //si existe un nombre ignorando su identificador
    boolean existsByNameIgnoreCaseAndIdNot(String name, Long id);
}