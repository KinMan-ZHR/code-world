package com.jiuaoedu.framework.communication.core.exception;

public class MessageHandlingException extends RuntimeException {
    public MessageHandlingException(String message) {
        super(message);
    }

    public MessageHandlingException(String message, Throwable cause) {
        super(message, cause);
    }
}