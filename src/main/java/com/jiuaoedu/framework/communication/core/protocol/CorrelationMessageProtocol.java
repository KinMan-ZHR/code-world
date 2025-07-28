package com.jiuaoedu.framework.communication.core.protocol;

import com.jiuaoedu.framework.communication.api.message.protocol.IMessageProtocol;
import com.jiuaoedu.framework.communication.utils.IdGenerator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基于Correlation ID的消息协议实现
 * 该协议通过特定格式在消息内容中嵌入correlationId，用于追踪消息链路
 *
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/25 12:00
 */
public class CorrelationMessageProtocol implements IMessageProtocol {

    private static final String PROTOCOL_KEY = "correlationId";
    private static final String PROTOCOL_NAME = "CorrelationMessageProtocol";
    private static final String PROTOCOL_VERSION = "1.0";
    private static final String SIGNATURE_PREFIX = "[" + PROTOCOL_KEY + "=";
    private static final String SIGNATURE_SUFFIX = "]";
    private static final String SIGNATURE_REGEX = "\\[" + PROTOCOL_KEY + "=[^]]+]";

    // 优化：将正则表达式编译为静态常量，避免重复编译
    private static final Pattern SIGNATURE_PATTERN = Pattern.compile(SIGNATURE_REGEX);
    private static final Pattern HAS_SIGNATURE_PATTERN = Pattern.compile(".*" + SIGNATURE_REGEX + ".*");

    @Override
    public String constructSignature() {
        return SIGNATURE_PREFIX + IdGenerator.generateUniqueId() + SIGNATURE_SUFFIX;
    }

    @Override
    public String extractSignature(String content) {
        if (content == null) return null;
        Matcher matcher = SIGNATURE_PATTERN.matcher(content);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    @Override
    public String extractValueFromSignature(String signature) {
        if (signature == null || !signature.startsWith(SIGNATURE_PREFIX) || !signature.endsWith(SIGNATURE_SUFFIX)) {
            return null;
        }
        return signature.substring(SIGNATURE_PREFIX.length(), signature.length() - SIGNATURE_SUFFIX.length());
    }

    @Override
    public String extractValue(String content) {
        String signature = extractSignature(content);
        return signature != null ? extractValueFromSignature(signature) : null;
    }

    @Override
    public boolean hasSignature(String content) {
        return content != null && HAS_SIGNATURE_PATTERN.matcher(content).matches();
    }

    @Override
    public String returnSignatureRemovedContent(String content) {
        if (content == null) return null;
        return content.replaceAll(SIGNATURE_REGEX, "");
    }

    @Override
    public boolean verifySignatureFormat(String signature) {
        return signature != null && signature.matches(SIGNATURE_REGEX);
    }

    @Override
    public String returnSignatureAddedContent(String content) {
        if (content == null) content = "";
        return constructSignature() + content;
    }

    @Override
    public String getProtocolName() {
        return PROTOCOL_NAME;
    }

    @Override
    public String getProtocolVersion() {
        return PROTOCOL_VERSION;
    }

    @Override
    public String getProtocolKey() {
        return PROTOCOL_KEY;
    }
}