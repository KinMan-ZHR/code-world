package com.jiuaoedu.framework.communication.core.exception;

public class RequestNotFoundException extends RuntimeException {
    public RequestNotFoundException(String message) {
        super(message);
    }
    public RequestNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
