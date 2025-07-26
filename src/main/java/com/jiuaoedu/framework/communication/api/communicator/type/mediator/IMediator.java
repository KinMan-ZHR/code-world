package com.jiuaoedu.framework.communication.api.communicator.type.mediator;

import com.jiuaoedu.framework.communication.api.communicator.Communicable;
import com.jiuaoedu.framework.communication.api.message.IMessage;
import com.jiuaoedu.framework.communication.core.pojo.Message;

// 2. 中介者接口
public interface IMediator {
    void registerComponent(Communicable component);
    void unregisterComponent(String componentId);
    void dispatchMessage(IMessage message);
}