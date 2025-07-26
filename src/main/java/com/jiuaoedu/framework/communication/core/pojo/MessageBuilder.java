package com.jiuaoedu.framework.communication.core.pojo;

import com.jiuaoedu.framework.communication.api.message.IMessage;
import com.jiuaoedu.framework.communication.api.message.IMessageBuilder;
import com.jiuaoedu.framework.communication.api.message.MessageType;
import com.jiuaoedu.framework.communication.core.protocol.DefaultMessageProtocol;
import com.jiuaoedu.framework.communication.api.message.protocol.IMessageProtocol;
import com.jiuaoedu.framework.communication.core.exception.MessageCreationException;

public class MessageBuilder implements IMessageBuilder {
    private String senderId;
    private String receiverId;
    private String content = "";
    private MessageType type;
    private String operationType = "";
    private final IMessageProtocol protocol;

    public MessageBuilder(IMessageProtocol protocol) {
        this.protocol = protocol;
    }

    public MessageBuilder() {
        this.protocol = new DefaultMessageProtocol();
    }

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

    public MessageBuilder signOperationType(String operationType) {
        this.operationType = operationType;
        return this;
    }

    public MessageBuilder ofType(MessageType type) {
        this.type = type;
        return this;
    }

    public MessageBuilder fromMessage(IMessage message) {
        this.senderId = message.getSenderId();
        this.receiverId = message.getReceiverId();
        this.content = message.getContent();
        this.type = message.getType();
        this.operationType = protocol.extractOperationType(content);
        return this;
    }

    public Message build() {
        content = protocol.generateContentWithOperationType(content, operationType);
        validate();
        return new Message(senderId, receiverId, content, type, protocol);
    }

    private void validate() {
        if (senderId == null || senderId.isEmpty()) {
            throw new MessageCreationException("Sender ID cannot be null or empty");
        }
        if (receiverId == null || receiverId.isEmpty()) {
            throw new MessageCreationException("Receiver ID cannot be null or empty");
        }
        if (type == null) {
            throw new MessageCreationException("Message type cannot be null");
        }
        if (!protocol.validateFormat(content)) {
            throw new MessageCreationException("Invalid message format");
        }
    }
}
