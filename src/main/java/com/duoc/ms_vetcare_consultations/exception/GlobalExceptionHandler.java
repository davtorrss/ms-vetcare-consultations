package com.duoc.ms_vetcare_consultations.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFound(
            ResourceNotFoundException exception
    ) {

        Map<String, String> response = new HashMap<>();

        response.put("error", "NOT_FOUND");
        response.put("message", exception.getMessage());

        return response;
    }

    @ExceptionHandler(InvalidStatusTransitionException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleInvalidTransition(
            InvalidStatusTransitionException exception
    ) {

        Map<String, String> response = new HashMap<>();

        response.put("error", "INVALID_STATUS_TRANSITION");
        response.put("message", exception.getMessage());

        return response;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(
            MethodArgumentNotValidException exception
    ) {

        Map<String, String> response = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        response.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return response;
    }
}