package com.sns.mutsasns.domain.entity;

import com.sns.mutsasns.domain.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
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

    public UserDto toDto(){
        return UserDto.builder()
                .id(this.id)
                .userName(this.userName)
                .build();
    }
}
