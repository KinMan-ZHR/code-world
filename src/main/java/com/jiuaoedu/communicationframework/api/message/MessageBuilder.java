package com.jiuaoedu.communicationframework.api.message;

public class MessageBuilder {
    private String senderId;
    private String receiverId;
    private String content;
    private MessageType type;

    public MessageBuilder from(String senderId) {
        this.senderId = senderId;
        return this;
    }

    public MessageBuilder to(String receiverId) {
        this.receiverId = receiverId;
        return this;
    }

    public MessageBuilder withContent(String content) {
        this.content = content;
        return this;
    }

    public MessageBuilder ofType(MessageType type) {
        this.type = type;
        return this;
    }

    public Message build() {
        validate();
        return new Message(senderId, receiverId, content, type);
    }

    private void validate() {
        if (senderId == null || senderId.isEmpty()) {
            throw new IllegalArgumentException("Sender ID cannot be null or empty");
        }
        if (receiverId == null || receiverId.isEmpty()) {
            throw new IllegalArgumentException("Receiver ID cannot be null or empty");
        }
        if (content == null) {
            throw new IllegalArgumentException("Content cannot be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("Message type cannot be null");
        }
    }
}
