package com.candidate.pks.exception;

import org.springframework.security.core.AuthenticationException;

public class IllegalArgumentException extends AuthenticationException {
    public IllegalArgumentException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public IllegalArgumentException(String msg) {
        super(msg);
    }
}
