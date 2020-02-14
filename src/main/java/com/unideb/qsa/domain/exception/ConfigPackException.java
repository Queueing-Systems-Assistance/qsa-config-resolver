package com.unideb.qsa.domain.exception;

/**
 * Exception for config packs.
 */
public class ConfigPackException extends InternalException {

    public ConfigPackException(String message) {
        super(message);
    }

    public ConfigPackException(String message, Throwable cause) {
        super(message, cause);
    }
}
