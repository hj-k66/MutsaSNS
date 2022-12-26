package com.sns.mutsasns.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserJoinResponse {
    private Long userId;
    private String userName;

}
