package com.api.cdms.command.domain.exception;

/**
 * Clase para manejar las excepciones de los humanos.
 */
public class HumanException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public HumanException(String message) {
        super(message);
    }
}
