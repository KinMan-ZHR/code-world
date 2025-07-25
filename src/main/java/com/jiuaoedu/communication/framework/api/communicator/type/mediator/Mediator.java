package com.jiuaoedu.communication.framework.api.communicator.type.mediator;

import com.jiuaoedu.communication.framework.api.communicator.Communicable;
import com.jiuaoedu.communication.framework.api.message.Message;

// 2. 中介者接口
public interface Mediator {
    void registerComponent(Communicable component);
    void unregisterComponent(String componentId);
    void dispatchMessage(Message message);
}