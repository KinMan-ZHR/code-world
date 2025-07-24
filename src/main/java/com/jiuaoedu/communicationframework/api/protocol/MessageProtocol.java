package com.jiuaoedu.communicationframework.api.protocol;

public interface MessageProtocol {
    String generateContentWithOperationType(String content, String operationType);
    String extractOperationType(String content);
    boolean validateFormat(String content);
}
