package com.example.edtecherp.enrollment.exception;

/**
 * Thrown when a business rule is violated (e.g., deleting a converted lead).
 * Mapped to HTTP 422 by GlobalExceptionHandler.
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
