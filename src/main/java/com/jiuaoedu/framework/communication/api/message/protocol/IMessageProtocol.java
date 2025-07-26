package com.jiuaoedu.framework.communication.api.message.protocol;

public interface IMessageProtocol {
    boolean validateFormat(String content);

    String generateContentWithCorrelationId(String content, String correlationId);

    String extractCorrelationId(String content);
}
