package com.sns.mutsasns.domain.entity;

import com.sns.mutsasns.domain.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
