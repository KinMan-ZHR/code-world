package com.jiuaoedu.framework.communication.core.protocol;

import com.jiuaoedu.framework.communication.api.message.protocol.IMessageProtocol;

import java.util.regex.Pattern;

public class DefaultMessageProtocol implements IMessageProtocol {
    private static final Pattern FORMAT_PATTERN = Pattern.compile("^[^:]+:.+$");

    @Override
    public String generateContentWithOperationType(String content, String operationType) {
        return operationType + ":" + content;
    }

    @Override
    public String extractOperationType(String content) {
        return content.split(":")[0];
    }

    @Override
    public boolean validateFormat(String content) {
        return FORMAT_PATTERN.matcher(content).matches();
    }
}