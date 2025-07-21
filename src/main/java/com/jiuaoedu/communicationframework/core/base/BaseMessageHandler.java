package com.jiuaoedu.communicationframework.core.base;

import com.jiuaoedu.communicationframework.api.communicator.MessageHandler;
import com.jiuaoedu.communicationframework.api.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseMessageHandler implements MessageHandler {
    private static final Logger log = LoggerFactory.getLogger(BaseMessageHandler.class);
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
            log.error("没有处理器可以处理消息: {}", message);
        }
    }

    protected abstract boolean canHandle(Message message);
    protected abstract void doHandle(Message message);
}
