package com.knubookStore.knubookStoreadmin.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode){
        super(errorCode.getMessage()); //RuntimeException 클래스의 생성자를 호출
        this.errorCode = errorCode;
    }
}
