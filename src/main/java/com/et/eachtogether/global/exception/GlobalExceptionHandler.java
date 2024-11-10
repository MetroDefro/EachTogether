package com.et.eachtogether.global.exception;

import com.et.eachtogether.global.dto.CommonErrorResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<CommonErrorResponseDto> handleException(final BusinessException exception, final HttpServletRequest request) {
        return ResponseEntity.status(exception.getStatus()).body(
            CommonErrorResponseDto.builder()
                .status(exception.getStatus())
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonErrorResponseDto> handleException(final MethodArgumentNotValidException exception, final HttpServletRequest request) {
        return ResponseEntity.status(exception.getStatusCode()).body(
            CommonErrorResponseDto.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message(exception.getMessage())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build()
        );
    }
}