package com.jiuaoedu.framework.communication.core.pojo;

import com.jiuaoedu.framework.communication.api.message.IMessage;
import com.jiuaoedu.framework.communication.api.message.MessageType;
import com.jiuaoedu.framework.communication.api.message.protocol.IMessageProtocol;
import com.jiuaoedu.framework.communication.utils.IdGenerator;

import java.time.LocalDateTime;

public class Message implements IMessage {
    private final String senderId;
    private final String receiverId;
    private final String content;
    private final MessageType type;
    private final LocalDateTime timestamp;
    private final String messageId;
    private final String operationType;
    private final IMessageProtocol protocol;

    protected Message(String senderId, String receiverId, String content, MessageType type, String operationType, IMessageProtocol protocol) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.messageId = generateUniqueId();
        this.protocol = protocol;
        this.operationType = operationType;
    }

    private String generateUniqueId() {
        return IdGenerator.generateUniqueId();
    }
    public String getSenderId() { return senderId; }
    public String getReceiverId() { return receiverId; }
    public String getContent() { return content; }
    public MessageType getType() { return type; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getMessageId() { return messageId; }
    public String getOperationType() {
        return operationType;
    }
    public IMessageProtocol getProtocol() {
        return protocol;
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId='" + messageId + '\'' +
                ", senderId='" + senderId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", type=" + type +
                ", timestamp=" + timestamp +
                ", content='" + content + '\'' +
                '}';
    }
}