package com.jiuaoedu.communicationframework.api.communicator;

import com.jiuaoedu.communicationframework.api.message.Message;

public interface MessageHandler {
    void handleMessage(Message message);
}