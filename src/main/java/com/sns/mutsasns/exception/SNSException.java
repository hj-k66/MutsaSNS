package com.sns.mutsasns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SNSException extends RuntimeException{
    private ErrorCode errorCode;

}
