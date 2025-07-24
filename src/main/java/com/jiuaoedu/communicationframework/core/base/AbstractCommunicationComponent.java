package com.jiuaoedu.communicationframework.core.base;

import com.jiuaoedu.communicationframework.api.communicator.Communicable;
import com.jiuaoedu.communicationframework.api.mediator.MediatorRegistrable;
import com.jiuaoedu.communicationframework.api.mediator.Mediator;
import com.jiuaoedu.communicationframework.api.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCommunicationComponent implements Communicable, MediatorRegistrable {
    private Mediator mediator;
    private final String componentId;
    private static final Logger logger = LoggerFactory.getLogger(AbstractCommunicationComponent.class);

    protected AbstractCommunicationComponent(String componentId) {
        this.componentId = componentId;
    }

    @Override
    public final void sendMessage(Message message) {
        if (mediator == null) {
            throw new IllegalStateException("Mediator not set. Cannot send message.");
        }
        logger.info("[发送] {} -> {} [{}]: {}",
                getComponentId(),
                message.getReceiverId(),
                message.getType(),
                message.getContent());
        mediator.dispatchMessage(message);
    }

    @Override
    public final void receiveMessage(Message message) {
        logger.info("[接收] {} -> {} [{}]: {}",
                getComponentId(),
                message.getSenderId(),
                message.getType(),
                message.getContent());
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
        logger.info("已注册组件: {} 到中介者", getComponentId());
    }

    @Override
    public final String getComponentId() {
        return componentId;
    }

    protected abstract void processMessage(Message message);
}
