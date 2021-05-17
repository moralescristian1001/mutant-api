package com.api.cdms.command.infrastructure.error;

import com.api.cdms.command.domain.exception.HumanException;
import com.api.cdms.command.domain.exception.MutantException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Clase que maneja todos los errores a nivel global de la aplicación (dependiendo su tipo de excepción).
 */
@ControllerAdvice
public class ErrorHandler {

    private static final Logger LOGGER_ERROR = LoggerFactory.getLogger(ErrorHandler.class);

    private static final String AN_ERROR_OCCURRED_PLEASE_CONTACT_THE_ADMINISTRATOR = "An error occurred please contact the administrator";

    private static final ConcurrentHashMap<String, Integer> STATUS_CODE = new ConcurrentHashMap<>();

    public ErrorHandler() {
        STATUS_CODE.put(MutantException.class.getSimpleName(), HttpStatus.BAD_REQUEST.value());
        STATUS_CODE.put(HumanException.class.getSimpleName(), HttpStatus.FORBIDDEN.value());
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Error> handleAllExceptions(Exception exception) {
        ResponseEntity<Error> result;

        String exceptionName = exception.getClass().getSimpleName();
        String message = exception.getMessage();
        Integer code = STATUS_CODE.get(exceptionName);

        LOGGER_ERROR.error(exceptionName, exception);
        if (code != null) {
            Error error = new Error(exceptionName, message);
            result = new ResponseEntity<>(error, HttpStatus.valueOf(code));
        } else {
            Error error = new Error(exceptionName, AN_ERROR_OCCURRED_PLEASE_CONTACT_THE_ADMINISTRATOR);
            result = new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return result;
    }
}