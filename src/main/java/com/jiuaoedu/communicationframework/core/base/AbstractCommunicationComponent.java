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
    public final String getComponentId() {
        return componentId;
    }

    @Override
    public final void registerToMediator(Mediator mediator) {
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

    protected abstract void processMessage(Message message);
}
