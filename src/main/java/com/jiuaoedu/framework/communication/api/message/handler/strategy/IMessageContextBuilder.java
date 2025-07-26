package com.jiuaoedu.framework.communication.api.message.handler.strategy;

import com.jiuaoedu.framework.communication.api.message.IMessage;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/26 11:53
 */
public interface IMessageContextBuilder {
    IMessageContext buildFromOriginalMessage(IMessage originalMessage);
}
