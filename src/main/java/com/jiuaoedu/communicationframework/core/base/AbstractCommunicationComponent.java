package com.jiuaoedu.communicationframework.core.base;

import com.jiuaoedu.communicationframework.api.communicator.Communicable;
import com.jiuaoedu.communicationframework.api.communicator.MediatorRegistrable;
import com.jiuaoedu.communicationframework.api.mediator.Mediator;
import com.jiuaoedu.communicationframework.api.message.Message;

public abstract class AbstractCommunicationComponent implements Communicable, MediatorRegistrable {
    private Mediator mediator;
    private final String componentId;

    public AbstractCommunicationComponent(String componentId) {
        this.componentId = componentId;
    }

    @Override
    public final void sendMessage(Message message) {
        if (mediator != null) {
            System.out.println(getComponentId() + " 发送" + message.getType() + "消息: " + message.getContent());
            mediator.dispatchMessage(message);
        } else {
            throw new IllegalStateException("中介者未设置，无法发送消息");
        }
    }

    @Override
    public final void setMediator(Mediator mediator) {
        this.mediator = mediator;
        registerToMediator(mediator);
    }

    @Override
    public final void registerToMediator(Mediator mediator) {
        mediator.registerComponent(this);
        System.out.println(getComponentId() + " 已注册到中介者");
    }

    @Override
    public final String getComponentId() {
        return componentId;
    }

    // 留给子类实现的业务逻辑
    protected abstract void processMessage(Message message);

    @Override
    public final void receiveMessage(Message message) {
        System.out.println(getComponentId() + " 收到" + message.getType() + "消息来自 " + 
                          message.getSenderId() + ": " + message.getContent());
        processMessage(message);
    }
}
