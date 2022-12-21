package com.sns.mutsasns.service;

import com.sns.mutsasns.domain.dto.UserDto;
import com.sns.mutsasns.domain.dto.UserJoinRequest;
import com.sns.mutsasns.domain.entity.User;
import com.sns.mutsasns.exception.ErrorCode;
import com.sns.mutsasns.exception.SNSException;
import com.sns.mutsasns.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public UserDto join(UserJoinRequest userJoinRequest){
        //예외 : userName 중복
        userRepository.findByUserName(userJoinRequest.getUserName())
                .ifPresent(user -> {
                    throw new SNSException(ErrorCode.DUPLICATED_USER_NAME);
                });
        User savedUser = userRepository.save(userJoinRequest.toEntity(encoder.encode(userJoinRequest.getPassword())));//password 인코딩해서 저장

        return savedUser.toDto();

    }

}
