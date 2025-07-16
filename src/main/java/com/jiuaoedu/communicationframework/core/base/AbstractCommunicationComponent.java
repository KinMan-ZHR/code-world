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
        if (mediator == null) {
            throw new IllegalStateException("Mediator not set. Cannot send message.");
        }
        System.out.println("[发送] " + getComponentId() + " -> " + message.getReceiverId() +
                " [" + message.getType() + "]: " + message.getContent());
        mediator.dispatchMessage(message);
    }

    @Override
    public final void receiveMessage(Message message) {
        System.out.println("[接收] " + getComponentId() + " <- " + message.getSenderId() +
                " [" + message.getType() + "]: " + message.getContent());
        processMessage(message);
    }

    @Override
    public final void setMediator(Mediator mediator) {
        this.mediator = mediator;
        registerToMediator(mediator);
    }

    @Override
    public final void registerToMediator(Mediator mediator) {
        mediator.registerComponent(this);
        System.out.println("已注册组件: " + getComponentId() + " 到中介者");
    }

    @Override
    public final String getComponentId() {

        return componentId;
    }

    protected abstract void processMessage(Message message);
}
