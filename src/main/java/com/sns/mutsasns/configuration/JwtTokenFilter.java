package com.sns.mutsasns.configuration;


import com.sns.mutsasns.domain.entity.User;
import com.sns.mutsasns.exception.ErrorCode;
import com.sns.mutsasns.exception.SNSException;
import com.sns.mutsasns.service.UserService;
import com.sns.mutsasns.utils.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

//OncePerRequestFilter:들어갈때마다 체킹한다.
@RequiredArgsConstructor
@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {
    private final UserService userService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //권한 여부 결정
        //권한 부여 X case
        //1. Token X —> Request할 때 Token을 안넣고 호출하는 경우
        //2. 만료된 Token일 경우
        //3. 적절하지 않은 Token일 경우

        final String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info("authorizationHeader:{}", authorizationHeader);

        String token;
        try {
            token = authorizationHeader.split(" ")[1];
        } catch (Exception e) {
            log.error("token 추출에 실패했습니다.");
            filterChain.doFilter(request,response);
            return;
        }

        if(authorizationHeader.startsWith("Bearer ")){
            try{
                String userName = JwtTokenUtil.getUserName(token, secretKey);
                //UserName Token에서 꺼내기
                log.info("userName : {}", userName);
                //UserDetail 가져오기 >> UserRole
                User user = userService.getUserByUserName(userName);
                log.info("userName:{}, userRole:{}", user.getUserName(), user.getRole());


                //문열어주기 >> 허용
                //Role 바인딩
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), null, List.of(new SimpleGrantedAuthority(user.getRole().name())));
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            }catch (IllegalArgumentException e){
                throw new SNSException(ErrorCode.INVALID_TOKEN);
            }catch (ExpiredJwtException e){
                log.info("만료된 토큰입니다.");
                throw new SNSException(ErrorCode.INVALID_TOKEN,"토큰 기한 만료");
            }catch (SignatureException e){
                log.info("서명이 일치하지 않습니다.");
                throw new SNSException(ErrorCode.INVALID_TOKEN,"서명 불일치");
            }
        }
        else{
            log.error("헤더를 가져오는 과정에서 에러가 났습니다. 헤더가 null이거나 잘못되었습니다.");
        }
        filterChain.doFilter(request, response);
    }
}
