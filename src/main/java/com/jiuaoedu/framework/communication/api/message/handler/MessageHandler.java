package com.jiuaoedu.framework.communication.api.message.handler;

import com.jiuaoedu.framework.communication.api.message.Message;

public interface MessageHandler {
    void handleMessage(Message message);
}