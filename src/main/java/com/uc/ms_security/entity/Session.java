package com.uc.ms_security.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "sessions")
@Getter
@Setter
@NoArgsConstructor
public class Session {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    private Date expiration;

    private String code2FA;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}