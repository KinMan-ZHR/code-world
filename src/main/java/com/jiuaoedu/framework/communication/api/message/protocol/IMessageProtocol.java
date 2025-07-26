package com.jiuaoedu.framework.communication.api.message.protocol;

public interface IMessageProtocol {
    String generateContentWithOperationType(String content, String operationType);
    String extractOperationType(String content);
    boolean validateFormat(String content);
}
