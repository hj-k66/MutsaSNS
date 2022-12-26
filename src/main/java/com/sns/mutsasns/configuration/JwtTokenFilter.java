package com.sns.mutsasns.configuration;


import com.sns.mutsasns.domain.entity.User;
import com.sns.mutsasns.service.UserService;
import com.sns.mutsasns.utils.JwtTokenUtil;
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

        //1. Token을 안가지고 오는 경우(request할 때 Token 안넣고 호출 or Bearer로 호출 x) 2. 만료된 Token
        if (authorizationHeader.startsWith("Bearer ") && !JwtTokenUtil.isExpired(token, secretKey)) {
            //UserName Token에서 꺼내기
            String userName = JwtTokenUtil.getUserName(token, secretKey);
            log.info("userName : {}", userName);
            //UserDetail 가져오기 >> UserRole
            User user = userService.getUserByUserName(userName);
            log.info("userName:{}, userRole:{}", user.getUserName(), user.getRole());


            //문열어주기 >> 허용
            //Role 바인딩
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), null, List.of(new SimpleGrantedAuthority(user.getRole().name())));
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        }
        filterChain.doFilter(request, response);
    }
}
