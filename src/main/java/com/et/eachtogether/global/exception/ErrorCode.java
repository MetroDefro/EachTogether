package com.et.eachtogether.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  // basic
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD REQUEST"),
  FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN"),
  UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED"),
  NOT_FOUND(HttpStatus.NOT_FOUND, "NOT FOUND"),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL SERVER ERROR"),

  // token
  TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "토큰을 찾을 수 없습니다."),
  TOKEN_EXPIRED(HttpStatus.BAD_REQUEST, "토큰이 만료되었습니다."),
  TOKEN_INVALID(HttpStatus.BAD_REQUEST, "토큰이 유효하지 않습니다."),
  REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "리프레쉬 토큰을 찾을 수 없습니다."),

  // user
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  ALREADY_EXISTING_USER(HttpStatus.BAD_REQUEST, "이미 가입한 이메일입니다."),
  EMAIL_PASSWORD_NOT_MATCH(HttpStatus.FORBIDDEN, "이메일 혹은 비밀번호가 일치하지 않습니다.");

  private final HttpStatus status;
  private final String message;
}
