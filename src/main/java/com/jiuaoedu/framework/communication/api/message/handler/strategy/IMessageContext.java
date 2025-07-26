package com.jiuaoedu.framework.communication.api.message.handler.strategy;

import com.jiuaoedu.framework.communication.api.message.IMessage;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/26 10:38
 */
public interface IMessageContext {
    IMessage getOriginalMessage();

    IMessage getProcessedMessage();

    void setProcessedMessage(IMessage processedMessage);

    String getTargetComponentId();

    void setTargetComponentId(String targetComponentId);

    boolean shouldForward();

    void setShouldForward(boolean shouldForward);

    boolean isHandled();

    void setHandled(boolean handled);

    Object getResult();

    void setResult(Object result);
}
