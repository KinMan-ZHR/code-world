package com.jiuaoedu.framework.communication.core.protocol;

import com.jiuaoedu.framework.communication.api.message.protocol.IMessageProtocol;

import java.util.regex.Pattern;

public class CorrelationMessageProtocol implements IMessageProtocol {
    private static final Pattern FORMAT_PATTERN = Pattern.compile("^[^:]+:.+$");

    @Override
    public boolean validateFormat(String content) {
        return FORMAT_PATTERN.matcher(content).matches();
    }

    @Override
    public String generateContentWithCorrelationId(String content, String correlationId) {
        return "correlationId=" + correlationId + ";" + content;
    }

    @Override
    public String extractCorrelationId(String content) {
        // 简单实现，实际应根据协议解析
        return content.split(";")[0].split("=")[1];
    }
}