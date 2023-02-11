package com.unideb.qsa.config.resolver.domain.exception;

/**
 * Abstract class for internal exceptions.
 */
abstract class InternalException extends RuntimeException {

    InternalException(String message) {
        super(message);
    }

    InternalException(String message, Throwable cause) {
        super(message, cause);
    }
}
