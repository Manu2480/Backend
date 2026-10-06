package com.uc.ms_security.repository;

//Encargado de ingresar a la base de datos e interactual con ella

import com.uc.ms_security.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/*Al extender JPA hay varios metodos (save(),findAll(),findById(),delete(),existsById())*/

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    //es como una consulta que si existe por email select * from [tabla] where amail [el que le mande]
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    /*el profile viene de entity, lo que hace es que (encuentre al
    usuario con el perfil dado el id del usuario)*/
    @EntityGraph(attributePaths = {"profile"})
    Optional<User> findWithProfileById(
            Long id
    );

    @EntityGraph(attributePaths = {"sessions"}) //hace referencia al atributo sessions de user
    Optional<User> findWithSessionsById(Long id);

    //Acá es donde hace el join, el join que se solicito desde el mapper
}
