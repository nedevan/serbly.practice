package com.example.practice.web.handler;

import com.example.practice.exception.AlreadyExitsException;
import com.example.practice.exception.EntityNotFoundException;
import com.example.practice.exception.RefreshTokenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class WebAppExceptionHandler {

    @ExceptionHandler(value = RefreshTokenException.class)
    public ResponseEntity<ErrorResponseBody> refreshTokenExceptionHandler(RefreshTokenException e, WebRequest webRequest) {
        return buildResponse(HttpStatus.FORBIDDEN, e, webRequest);
    }

    @ExceptionHandler(value = AlreadyExitsException.class)
    public ResponseEntity<ErrorResponseBody> alreadyExitsExceptionHandler(AlreadyExitsException e, WebRequest webRequest) {
        return buildResponse(HttpStatus.BAD_REQUEST, e, webRequest);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseBody> entityNotFoundExceptionHandler(EntityNotFoundException e, WebRequest webRequest) {
        return buildResponse(HttpStatus.NOT_FOUND, e, webRequest);
    }

    private ResponseEntity<ErrorResponseBody> buildResponse(HttpStatus httpStatus, Exception e, WebRequest webRequest) {
        return ResponseEntity.status(httpStatus)
                .body(ErrorResponseBody.builder()
                        .message(e.getMessage())
                        .description(webRequest.getDescription(false))
                        .build());
    }
}
