package com.jiuaoedu.communicationframework.api.communicator;

import com.jiuaoedu.communicationframework.api.mediator.Mediator;
import com.jiuaoedu.communicationframework.api.message.Message;

import java.util.EventListener;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/16 14:38
 */
public interface Communicable {
    void sendMessage(Message message);
    void receiveMessage(Message message);
    void setMediator(Mediator mediator);
    String getComponentId();
}
