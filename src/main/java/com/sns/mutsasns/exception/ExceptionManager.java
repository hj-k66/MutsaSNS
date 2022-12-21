package com.sns.mutsasns.exception;

import com.sns.mutsasns.domain.dto.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(SNSException.class)
    public ResponseEntity<?> snsExceptionHandler(SNSException e){
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(Response.error(e.getErrorCode().getErrorResult()));
    }

}
