package com.duoc.ms_vetcare_consultations.exception;

public class InvalidStatusTransitionException
        extends RuntimeException {

    public InvalidStatusTransitionException(String message) {
        super(message);
    }
}