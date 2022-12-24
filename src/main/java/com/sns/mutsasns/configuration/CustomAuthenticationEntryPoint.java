package com.sns.mutsasns.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sns.mutsasns.domain.dto.Response;
import com.sns.mutsasns.exception.ErrorCode;
import com.sns.mutsasns.exception.ErrorResult;
import com.sns.mutsasns.exception.SNSException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("인증 실패");
        ObjectMapper objectMapper = new ObjectMapper();
        SNSException snsException = new SNSException(ErrorCode.INVALID_TOKEN);
        Response<ErrorResult> error = Response.error(snsException.getErrorCode().getErrorResult());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(objectMapper.writeValueAsString(error)); //Response객체를 response의 바디값으로 파싱
    }
}
