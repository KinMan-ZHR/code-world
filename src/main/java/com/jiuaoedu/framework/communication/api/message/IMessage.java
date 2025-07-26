package com.jiuaoedu.framework.communication.api.message;

import com.jiuaoedu.framework.communication.api.message.protocol.IMessageProtocol;

import java.time.LocalDateTime;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/26 10:32
 */
public interface IMessage {
    String getSenderId();
    String getReceiverId();
    String getContent();
    MessageType getType();
    LocalDateTime getTimestamp();
    String getMessageId();
    String getOperationType();
    IMessageProtocol getProtocol();
}
