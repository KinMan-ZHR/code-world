package com.jiuaoedu.framework.communication.api.message.context;

import com.jiuaoedu.framework.communication.api.message.IMessage;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/26 10:38
 */
public interface IMessageContext {
    IMessage getOriginalMessage();

    IMessage getProcessedMessage();

    void setProcessedMessage(IMessage processedMessage);

    IMessageStateTracker getMessageStateTracker();

    void setMessageStateTracker(IMessageStateTracker messageStateTracker);

    /**
     * 是否需要转发
     *
     * @return true: 需要转发
     */
    boolean shouldForward();

    void setShouldForward(boolean shouldForward);

    /**
     * 是否已经处理
     *
     * @return true: 已经处理
     */
    boolean isHandled();

    void setHandled(boolean handled);

    Object getResult();

    void setResult(Object result);
}
