package com.jiuaoedu.communication.framework.api.communicator.extension;

import com.jiuaoedu.communication.framework.api.communicator.Communicable;
import com.jiuaoedu.communication.framework.api.message.Message;

public interface StorableCommunicable extends Communicable, MessageStorable {
    @Override
    default void receiveMessage(Message message) {
        storeValue(message);  // 强制存储（所有实现类必须遵守）
        doReceiveMessage(message);  // 委托给具体实现
    }
    
    void doReceiveMessage(Message message);  // 由实现类实现后续处理
}