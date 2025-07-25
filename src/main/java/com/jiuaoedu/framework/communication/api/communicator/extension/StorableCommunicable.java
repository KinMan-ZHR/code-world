package com.jiuaoedu.framework.communication.api.communicator.extension;

import com.jiuaoedu.framework.communication.api.communicator.Communicable;
import com.jiuaoedu.framework.communication.api.message.Message;

/**
 * 接口增强：可存储消息的通信组件
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/25 14:59
 */
public interface StorableCommunicable extends Communicable, MessageStorable {
    @Override
    default void receiveMessage(Message message) {
        storeValue(message);  // 强制存储（所有实现类必须遵守）
        doReceiveMessage(message);  // 委托给具体实现
    }
    
    void doReceiveMessage(Message message);  // 由实现类实现后续处理
}