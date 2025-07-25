package com.jiuaoedu.communication.framework.api.communicator;

import com.jiuaoedu.communication.framework.api.message.Message;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/16 14:38
 */
public interface Communicable {
    void sendMessage(Message message);
    void receiveMessage(Message message);
    String getComponentId();
}
