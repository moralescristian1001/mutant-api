package com.api.cdms.command.infrastructure.error;

public class Error {

    private String exceptionName;
    private String message;

    public Error(String exceptionName, String message) {
        this.exceptionName = exceptionName;
        this.message = message;
    }

    public String getExceptionName() {
        return exceptionName;
    }

    public String getMessage() {
        return message;
    }

}
