package com.example.dto;

import java.io.Serializable;

/**
 * Represents a field error JSON object
 *
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 22/07/16
 */
public class FieldError implements Serializable {

    private String field;

    private String errorMessage;

    public FieldError(String field, String errorMessage) {
        this.field = field;
        this.errorMessage = errorMessage;
    }

    public String getField() {
        return field;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}