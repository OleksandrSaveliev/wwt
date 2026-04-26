package com.wwt.dataapi.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        int status,
        String error,
        String path,
        LocalDateTime timestamp,
        Map<String, String> fieldErrors
) {
    public static ErrorResponse of(int status, String error, String path, Map<String, String> fieldErrors) {
        return new ErrorResponse(status, error, path, LocalDateTime.now(), fieldErrors);
    }

    public static ErrorResponse of(int status, String error, String path) {
        return new ErrorResponse(status, error, path, LocalDateTime.now(), Map.of());
    }
}