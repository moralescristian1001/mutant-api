package com.api.cdms.command.domain.exception;

/**
 * Clase para menejar las excepciones de los mutantes.
 */
public class MutantException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public MutantException(String message) {
        super(message);
    }
}
