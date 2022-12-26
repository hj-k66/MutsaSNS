package com.sns.mutsasns.fixture;

import com.sns.mutsasns.domain.entity.User;
import com.sns.mutsasns.domain.entity.UserRole;

public class UserFixture {
    public static User get(String userName, String password){
        return User.builder()
                .id(1L)
                .userName(userName)
                .password(password)
                .role(UserRole.USER)
                .build();
    }
}
