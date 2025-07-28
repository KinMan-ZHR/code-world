package com.jiuaoedu.framework.communication.core.communicator.mediator;

import com.jiuaoedu.framework.communication.api.communicator.Communicable;
import com.jiuaoedu.framework.communication.api.communicator.type.mediator.MediatorRegistrable;
import com.jiuaoedu.framework.communication.api.communicator.type.mediator.IMediator;
import com.jiuaoedu.framework.communication.api.message.IMessage;
import com.jiuaoedu.framework.communication.core.exception.CommunicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public abstract class MediatorRegistrableCommunicationComponent implements Communicable, MediatorRegistrable {
    private IMediator mediator;
    private final String componentId;
    private static final Logger logger = LoggerFactory.getLogger(MediatorRegistrableCommunicationComponent.class);

    protected MediatorRegistrableCommunicationComponent(String componentId) {
        this.componentId = componentId;
    }

    @Override
    public final void sendMessage(IMessage message) {
        if (mediator == null) {
            throw new CommunicationException("Mediator not set. Cannot send message.");
        }
        
        // 防止发送消息给自己
        if (componentId.equals(message.getReceiverId())) {
            logger.warn("尝试发送消息给自己，已阻止: {}", message.getMessageId());
            return;
        }
        
        logger.info("[发送] {} -> {} [{}]: {}",
                getComponentId(),
                message.getReceiverId(),
                message.getType(),
                message.getContent());
        mediator.dispatchMessage(message);
    }

    @Override
    public final void receiveMessage(IMessage message) {
        logger.info("[接收] {} <- {} [{}]: {}",
                getComponentId(),
                message.getSenderId(),
                message.getType(),
                message.getContent());
        processMessage(message);
    }

    @Override
    public final String getComponentId() {
        return componentId;
    }

    @Override
    public final void registerToMediator(IMediator mediator) {
        this.mediator = mediator;
        mediator.registerComponent(this);
    }

    @Override
    public final void unregisterFromMediator() {
        if (mediator != null) {
            mediator.unregisterComponent(componentId);
            this.mediator = null;
        }
    }

    protected abstract void processMessage(IMessage message);
}
