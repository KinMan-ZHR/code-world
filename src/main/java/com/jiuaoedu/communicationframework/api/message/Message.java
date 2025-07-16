package com.jiuaoedu.communicationframework.api.message;


import java.time.LocalDateTime;

public class Message {
    private final String senderId;
    private final String receiverId;
    private final String content;
    private final MessageType type;
    private final LocalDateTime timestamp;
    private final String messageId;

    public Message(String senderId, String receiverId, String content, MessageType type) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.content = content;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.messageId = generateUniqueId();
    }

    private String generateUniqueId() {
        return java.util.UUID.randomUUID().toString();
    }

    // Getters
    public String getSenderId() { return senderId; }
    public String getReceiverId() { return receiverId; }
    public String getContent() { return content; }
    public MessageType getType() { return type; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getMessageId() { return messageId; }

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