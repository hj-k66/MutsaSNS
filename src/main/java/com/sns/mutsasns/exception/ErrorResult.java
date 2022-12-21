package com.sns.mutsasns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResult {
    private ErrorCode errorCode;
    private String message;
}
