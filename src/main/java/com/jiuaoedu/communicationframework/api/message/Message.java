package com.jiuaoedu.communicationframework.api.message;

import com.jiuaoedu.communicationframework.api.protocol.MessageProtocol;
import com.jiuaoedu.communicationframework.utils.IdGenerator;

import java.time.LocalDateTime;

public class Message {
    private final String senderId;
    private final String receiverId;
    private final String content;
    private final MessageType type;
    private final LocalDateTime timestamp;
    private final String messageId;
    private final MessageProtocol protocol;

    protected Message(String senderId, String receiverId, String content, MessageType type, MessageProtocol protocol) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.messageId = generateUniqueId();
        this.protocol = protocol;
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
        return protocol.extractOperationType(content);
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