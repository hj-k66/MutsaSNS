package com.sns.mutsasns.domain.entity;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate deletedAt;
    private LocalDate registeredAt;
    private LocalDate updatedAt;

    @Column(unique = true)
    private String userName;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole role;
}
