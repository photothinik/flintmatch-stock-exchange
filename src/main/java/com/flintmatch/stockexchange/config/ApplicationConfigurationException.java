package com.flintmatch.stockexchange.config;

public class ApplicationConfigurationException extends RuntimeException {

    public ApplicationConfigurationException() {
    }

    public ApplicationConfigurationException(String message) {
        super(message);
    }

    public ApplicationConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationConfigurationException(Throwable cause) {
        super(cause);
    }

    public ApplicationConfigurationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
