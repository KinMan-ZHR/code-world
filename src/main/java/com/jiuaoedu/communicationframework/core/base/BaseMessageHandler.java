package com.jiuaoedu.communicationframework.core.base;

import com.jiuaoedu.communicationframework.api.communicator.MessageHandler;
import com.jiuaoedu.communicationframework.api.message.Message;
import com.jiuaoedu.communicationframework.core.exception.MessageHandlingException;

public abstract class BaseMessageHandler implements MessageHandler {
    private MessageHandler nextHandler;

    public BaseMessageHandler setNextHandler(MessageHandler nextHandler) {
        this.nextHandler = nextHandler;
        return this;
    }

    @Override
    public void handleMessage(Message message) {
        if (canHandle(message)) {
            doHandle(message);
        } else if (nextHandler != null) {
            nextHandler.handleMessage(message);
        } else {
            throw new MessageHandlingException("没有处理器可以处理消息: " + message);
        }
    }

    protected abstract boolean canHandle(Message message);
    protected abstract void doHandle(Message message);
}
