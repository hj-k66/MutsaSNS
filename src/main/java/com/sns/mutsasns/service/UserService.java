package com.sns.mutsasns.service;

import com.sns.mutsasns.domain.dto.user.UserDto;
import com.sns.mutsasns.domain.dto.user.UserJoinRequest;
import com.sns.mutsasns.domain.dto.user.UserLoginRequest;
import com.sns.mutsasns.domain.entity.User;
import com.sns.mutsasns.exception.ErrorCode;
import com.sns.mutsasns.exception.SNSException;
import com.sns.mutsasns.respository.UserRepository;
import com.sns.mutsasns.utils.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.token.secret}")
    private String secretKey;
    private long expireTimeMs = 1000 * 60 * 60; //1시간

    public User getUserByUserName(String userName){
        return userRepository.findByUserName(userName)
                .orElseThrow(()->new SNSException(ErrorCode.USERNAME_NOT_FOUND));
    }

    public UserDto join(UserJoinRequest userJoinRequest){
        //예외 : userName 중복
        userRepository.findByUserName(userJoinRequest.getUserName())
                .ifPresent(user -> {
                    throw new SNSException(ErrorCode.DUPLICATED_USER_NAME);
                });
        User savedUser = userRepository.save(userJoinRequest.toEntity(encoder.encode(userJoinRequest.getPassword())));//password 인코딩해서 저장

        return savedUser.toDto();

    }

    public String login(UserLoginRequest userLoginRequest) {
        String userName = userLoginRequest.getUserName();
        //해당 userName이 없는 경우
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new SNSException(ErrorCode.USERNAME_NOT_FOUND));
        //해당 password가 틀린 경우
        if(!encoder.matches(userLoginRequest.getPassword(),user.getPassword())){
            throw new SNSException(ErrorCode.INVALID_PASSWORD);
        }
        return JwtTokenUtil.createToken(user.getUserName(),secretKey,expireTimeMs);
    }
}
