package com.jiuaoedu.communication.framework.core.exception;

public class CommunicationException extends RuntimeException {
    public CommunicationException(String message) {
        super(message);
    }

    public CommunicationException(String message, Throwable cause) {
        super(message, cause);
    }
}