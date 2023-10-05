package com.library.api.exceptions.http_responses;

import lombok.Builder;

import java.util.Date;

@Builder
public record ExceptionResponse(
        Date timestamp,
        String message,
        String details
) {
}
