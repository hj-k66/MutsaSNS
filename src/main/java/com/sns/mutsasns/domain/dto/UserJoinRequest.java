package com.sns.mutsasns.domain.dto;

import com.sns.mutsasns.domain.entity.User;
import com.sns.mutsasns.domain.entity.UserRole;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class UserJoinRequest {
    private String userName;
    private String password;

    public User toEntity(String password){
        return User.builder()
                .userName(this.userName)
                .password(password)
                .role(UserRole.USER) //default = USER
                .registeredAt(LocalDate.now())
                .build();

    }
}
