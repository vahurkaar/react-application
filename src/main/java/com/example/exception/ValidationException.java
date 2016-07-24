package com.example.exception;

import org.springframework.validation.Errors;

/**
 * Custom exception class for passing validation errors to the
 * {@link com.example.controller.RestExceptionHandler} component.
 *
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 22/07/16
 */
public class ValidationException extends Exception {

    private Errors errors;

    public ValidationException(Errors errors) {
        this.errors = errors;
    }

    public Errors getErrors() {
        return errors;
    }
}
