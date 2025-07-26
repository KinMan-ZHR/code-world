package com.jiuaoedu.framework.communication.api.message;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/26 10:57
 */
public interface IMessageBuilder {
    IMessage build();
    IMessageBuilder fromMessage(IMessage message);
    IMessageBuilder from(String senderId);
    IMessageBuilder to(String receiverId);
    IMessageBuilder withContent(String content);
    IMessageBuilder signOperationType(String operationType);
    IMessageBuilder ofType(MessageType type);
}
