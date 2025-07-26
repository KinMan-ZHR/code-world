package com.jiuaoedu.framework.communication.core.pojo;

import com.jiuaoedu.framework.communication.api.message.IMessage;
import com.jiuaoedu.framework.communication.api.message.handler.strategy.IMessageContext;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/26 10:19
 */
public class MessageContext implements IMessageContext {
    private final IMessage originalMessage;
    private IMessage processedMessage;
    private String targetComponentId;
    private boolean shouldForward;
    private boolean isHandled;
    private Object result;

    public MessageContext(IMessage originalMessage) {
        this.originalMessage = originalMessage;
        this.processedMessage = originalMessage;
        this.shouldForward = true;
        this.isHandled = false;
    }

    @Override
    public IMessage getOriginalMessage() {
        return originalMessage;
    }

    @Override
    public IMessage getProcessedMessage() {
        return processedMessage;
    }

    @Override
    public void setProcessedMessage(IMessage processedMessage) {
        this.processedMessage = processedMessage;
    }

    @Override
    public String getTargetComponentId() {
        return targetComponentId;
    }

    @Override
    public void setTargetComponentId(String targetComponentId) {
        this.targetComponentId = targetComponentId;
    }

    @Override
    public boolean shouldForward() {
        return shouldForward;
    }

    @Override
    public void setShouldForward(boolean shouldForward) {
        this.shouldForward = shouldForward;
    }

    @Override
    public boolean isHandled() {
        return isHandled;
    }

    @Override
    public void setHandled(boolean handled) {
        isHandled = handled;
    }

    @Override
    public Object getResult() {
        return result;
    }

    @Override
    public void setResult(Object result) {
        this.result = result;
    }
}
