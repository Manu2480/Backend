package com.uc.ms_security.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(
            nullable = false,
            length = 30
    )
    private String phone;

    @Column(
            name = "birth_date",
            nullable = false
    )
    @Temporal(TemporalType.DATE)
    private Date birthDate;

    //relación 1-1 en la base de datos
    @OneToOne(
            /*lazy carga por partes, para que no haga joins cada que
            se pieda el usuario, no se cargue todo el perfil*/
            fetch = FetchType.LAZY
    )
    //un join enlaza, arrastra datos a una tabla de otra table
    @JoinColumn(
            name = "user_id",
            nullable = false,
            unique = true //debe ser unique para que si sea solo 1-1
    )
    private User user; //de donde viene
}