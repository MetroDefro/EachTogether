package com.et.eachtogether.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BusinessException extends RuntimeException {
  private final HttpStatus status;
  private final String message;

  public BusinessException(ErrorCode errorCode) {
    this.status = errorCode.getStatus();
    this.message = errorCode.getMessage();
  }
}
