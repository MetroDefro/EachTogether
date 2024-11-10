package com.et.eachtogether.global.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record CommonErrorResponseDto(HttpStatus status, String message, String path, LocalDateTime timestamp) {
}
