package com.sns.mutsasns.domain.dto.user;

import com.sns.mutsasns.domain.entity.User;
import com.sns.mutsasns.domain.entity.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserJoinRequest {
    private String userName;
    private String password;


    public User toEntity(String password){
        return User.builder()
                .userName(this.userName)
                .password(password)
                .role(UserRole.USER) //default = USER
                .build();

    }
}
