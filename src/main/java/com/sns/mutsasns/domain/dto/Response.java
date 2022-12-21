package com.sns.mutsasns.domain.dto;


import com.sns.mutsasns.exception.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Response<T> {
    private String resultCode;
    private T result;

    public static <T> Response<T> success(T result){
        return new Response<>("SUCCESS",result);
    }

    public static Response<ErrorResult> error(ErrorResult errorResult){
        return new Response<>("ERROR",errorResult);
    }
}
