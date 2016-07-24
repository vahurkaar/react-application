package com.example.controller;

import com.example.dto.FieldError;
import com.example.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The application's exception handler class, which:
 * <ul>
 *  <li>converts the internal validation field errors to a usable JSON format and sends
 *      it back to the client in the response</li>
 *  <li>in case of an unexpected exception, sends the error message back to the client in the response</li>
 * </ul>
 *
 * @author Vahur Kaar (vahurkaar@gmail.com)
 * @since 21/07/16
 */
@ControllerAdvice
public class RestExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestExceptionHandler.class);

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public List<FieldError> handleException(ValidationException exception) {
        Errors errors = exception.getErrors();

        if (errors != null) {
            List<FieldError> convertedFieldErrors = errors.getFieldErrors().stream()
                    .map(fieldError -> new FieldError(fieldError.getField(), resolveErrorMessage(fieldError)))
                    .collect(Collectors.toList());
            convertedFieldErrors.sort((first, second) -> first.getField().compareTo(second.getField()));
            return convertedFieldErrors;
        }

        return new ArrayList<>();
    }

    private String resolveErrorMessage(org.springframework.validation.FieldError fieldError) {
        for (String errorCode : fieldError.getCodes()) {
            try {
                return messageSource.getMessage(errorCode, fieldError.getArguments(), LocaleContextHolder.getLocale());
            } catch (NoSuchMessageException e) {
                // Try the next message code
            }
        }

        return fieldError.getDefaultMessage();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleAllException(Exception exception) {
        LOGGER.error("Exception!", exception);
        return exception.getMessage();
    }
}
