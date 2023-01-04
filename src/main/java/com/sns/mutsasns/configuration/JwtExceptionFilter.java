package com.sns.mutsasns.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sns.mutsasns.domain.dto.Response;
import com.sns.mutsasns.exception.ErrorResult;
import com.sns.mutsasns.exception.SNSException;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        }catch (SNSException exception){
            setErrorResponse(HttpStatus.UNAUTHORIZED, response, exception);
        }
    }

    private void setErrorResponse(HttpStatus status, HttpServletResponse res, SNSException exception) throws IOException{
        ObjectMapper objectMapper = new ObjectMapper();
        ErrorResult errorResult = new ErrorResult(exception.getErrorCode(), exception.toString());
        Response<ErrorResult> error = Response.error(errorResult);


        res.setStatus(status.value());
        res.setContentType("application/json");
        res.setCharacterEncoding("utf-8");
        res.getWriter().write(objectMapper.writeValueAsString(error)); //Response객체를 response의 바디값으로 파싱

    }
}
