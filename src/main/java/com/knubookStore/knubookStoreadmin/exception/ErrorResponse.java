package com.knubookStore.knubookStoreadmin.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Builder
@Getter
public class ErrorResponse {
    private final LocalDateTime dateTime = LocalDateTime.now();
    private final String status;
    private final String code;
    private final String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode){
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ErrorResponse.builder()
                        .status(errorCode.getHttpStatus().name())
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }
}