package com.jiuaoedu.communicationframework.api.mediator;

import com.jiuaoedu.communicationframework.api.communicator.Communicable;
import com.jiuaoedu.communicationframework.api.message.Message;

// 2. 中介者接口
public interface Mediator {
    void registerComponent(Communicable component);
    void unregisterComponent(String componentId);
    void dispatchMessage(Message message);
}