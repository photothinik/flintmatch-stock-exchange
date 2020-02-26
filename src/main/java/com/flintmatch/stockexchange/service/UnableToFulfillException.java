package com.flintmatch.stockexchange.service;

public class UnableToFulfillException extends Exception {

    public UnableToFulfillException() {
    }

    public UnableToFulfillException(String message) {
        super(message);
    }

    public UnableToFulfillException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnableToFulfillException(Throwable cause) {
        super(cause);
    }

    public UnableToFulfillException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
