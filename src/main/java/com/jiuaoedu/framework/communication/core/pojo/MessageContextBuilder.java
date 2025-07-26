package com.jiuaoedu.framework.communication.core.pojo;

import com.jiuaoedu.framework.communication.api.message.IMessage;
import com.jiuaoedu.framework.communication.api.message.context.IMessageContextBuilder;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/26 11:54
 */
public class MessageContextBuilder implements IMessageContextBuilder {
    @Override
    public MessageContext buildFromOriginalMessage(IMessage originalMessage) {
        return new MessageContext(originalMessage);
    }
}
