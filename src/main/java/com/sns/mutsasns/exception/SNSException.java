package com.sns.mutsasns.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SNSException extends RuntimeException{
    private ErrorCode errorCode;
    private String message;

    @Builder
    public SNSException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }


    @Override
    public String toString() {
        return errorCode.getMessage()
                + this.message;
    }
}
