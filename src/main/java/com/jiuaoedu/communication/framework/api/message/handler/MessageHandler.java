package com.jiuaoedu.communication.framework.api.message.handler;

import com.jiuaoedu.communication.framework.api.message.Message;

public interface MessageHandler {
    void handleMessage(Message message);
}