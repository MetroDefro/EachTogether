package com.et.eachtogether.global.dto;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record CommonResponseDto<T>(HttpStatus status, String message, T data) {

}
