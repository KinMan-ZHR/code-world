package com.jiuaoedu.framework.communication.core.communicator.mediator;

import com.jiuaoedu.framework.communication.api.communicator.Communicable;
import com.jiuaoedu.framework.communication.api.communicator.type.mediator.MediatorRegistrable;
import com.jiuaoedu.framework.communication.api.communicator.type.mediator.IMediator;
import com.jiuaoedu.framework.communication.api.message.IMessage;
import com.jiuaoedu.framework.communication.core.exception.CommunicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MediatorRegistrableCommunicationComponent implements Communicable, MediatorRegistrable {
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
        if (this.mediator != null) {
            logger.warn("组件已注册到中介者，重复注册将覆盖之前的中介者");
        }
        this.mediator = mediator;           // 设置中介者引用
        mediator.registerComponent(this);   // 通知中介者注册
        onRegistered();                     // 注册后的回调（子类可扩展）
    }

    /**
     * 注册后的回调方法，子类可扩展
     */
    protected void onRegistered() {
        // 默认空实现，子类可重写
    }

    /**
     * 处理消息的方法，子类可扩展
     *
     * @param message 要处理的消息
     */
    protected void processMessage(IMessage message){
        // 默认空实现，子类可重写
    }
}
