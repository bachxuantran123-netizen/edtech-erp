package com.example.edtecherp.enrollment.exception;

/**
 * Thrown when a resource already exists (e.g., duplicate email).
 * Mapped to HTTP 409 by GlobalExceptionHandler.
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }
}
