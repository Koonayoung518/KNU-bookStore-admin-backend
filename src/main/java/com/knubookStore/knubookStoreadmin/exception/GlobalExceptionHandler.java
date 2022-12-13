package com.knubookStore.knubookStoreadmin.exception;

import com.knubookStore.knubookStoreadmin.exception.errors.CustomException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponse> handleCustomException(CustomException e){
        return ErrorResponse.toResponseEntity(e.getErrorCode());
    }
}
