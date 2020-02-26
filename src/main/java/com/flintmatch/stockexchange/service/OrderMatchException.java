package com.flintmatch.stockexchange.service;

public class OrderMatchException extends Exception {

    public OrderMatchException() {
    }

    public OrderMatchException(String message) {
        super(message);
    }

    public OrderMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public OrderMatchException(Throwable cause) {
        super(cause);
    }

    public OrderMatchException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
