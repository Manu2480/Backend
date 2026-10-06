package com.uc.ms_security.entity;

import jakarta.persistence.*; // jakarta: es para temas de validaciones
import lombok.Getter; // lombok: Es una libreria que en tiempo de ejecución sabe que las clases necesitan un get y un set y aunque no esten en la clase se podran manipular
import lombok.NoArgsConstructor; // lombok: También crea el constructor en tiempo de ejecución, entonces se activa cuando se van a crear los objetos
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/*Si hubiera que cambiar la base a una NoSQL hay que hacer de nuevo esta entidad y usar
* los decoradores propios de la base NoSQL*/
// decoradores: @ -> indican que eso se va a traducir como una tabla en bases de datos (relacional)
@Entity
@Table(name = "users")// hay una entidad llamada user pero la tabla se llamara users
@Getter
@Setter
@NoArgsConstructor

public class User {

    @Id// en esta entidad id es PK
    @GeneratedValue(// generación de id autoincrementable
            strategy = GenerationType.IDENTITY
    )
    //(lo ideal es que id sea numerico en vez de varchar porque ese cuesta más en eficiencia)
    private Long id; // long es una capacidad más grande que int

    @Column(
            nullable = false, // este campo no puede ser nulo
            length = 100 //maximo de 100
    )
    private String name;

    @Column(
            nullable = false,
            unique = true, //unico
            length = 150
    )
    private String email;

    @Column(
            nullable = false
    )
    private String password;

    //hace parte de la relación bidireccional 1-1 de user y profile
    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true, //permite eliminar el perfil de un usuario
            fetch = FetchType.LAZY
    )
    private Profile profile;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Session> sessions = new ArrayList<>();

    public void addSession(Session session) {
        sessions.add(session);
        session.setUser(this);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
        session.setUser(null);
    }
}
