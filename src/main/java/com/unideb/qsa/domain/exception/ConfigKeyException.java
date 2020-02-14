package com.unideb.qsa.domain.exception;

/**
 * Exception for config key exceptions.
 */
public class ConfigKeyException extends InternalException {

    public ConfigKeyException(String message) {
        super(message);
    }

    public ConfigKeyException(String message, Throwable cause) {
        super(message, cause);
    }
}
