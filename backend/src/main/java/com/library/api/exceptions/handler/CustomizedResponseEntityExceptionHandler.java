package com.library.api.exceptions.handler;

import com.library.api.exceptions.ObjectAlreadyExistsWithException;
import com.library.api.exceptions.ObjectNotFoundException;
import com.library.api.exceptions.http_responses.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .timestamp(new Date())
                                .details(request.getDescription(false))
                                .message(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(ObjectAlreadyExistsWithException.class)
    public final ResponseEntity<ExceptionResponse> handleObjectAlreadyExistsWithExceptions(ObjectAlreadyExistsWithException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .timestamp(new Date())
                                .details(request.getDescription(false))
                                .message(ex.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleObjectAlreadyExistsWithExceptions(ObjectNotFoundException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(
                        ExceptionResponse.builder()
                                .timestamp(new Date())
                                .details(request.getDescription(false))
                                .message(ex.getMessage())
                                .build()
                );
    }
}
