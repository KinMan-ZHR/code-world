package com.jiuaoedu.framework.communication.core.communicator.mediator.mediators;

import com.jiuaoedu.framework.communication.api.communicator.Communicable;
import com.jiuaoedu.framework.communication.api.communicator.type.mediator.IMediator;
import com.jiuaoedu.framework.communication.api.message.IMessage;
import com.jiuaoedu.framework.communication.api.message.MessageType;
import com.jiuaoedu.framework.communication.api.message.context.IMessageContext;
import com.jiuaoedu.framework.communication.api.message.context.IMessageContextBuilder;
import com.jiuaoedu.framework.communication.api.message.context.handler.strategy.IMessageStrategyChain;
import com.jiuaoedu.framework.communication.core.pojo.Message;
import com.jiuaoedu.framework.communication.core.pojo.MessageBuilder;
import com.jiuaoedu.framework.communication.core.exception.MessageHandlingException;
import com.jiuaoedu.framework.communication.core.pojo.MessageContextBuilder;
import com.jiuaoedu.framework.communication.extension.strategy.MessageStrategyChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SystemMediator implements IMediator {
    private static final Logger log = LoggerFactory.getLogger(SystemMediator.class);
    private final Map<String, Communicable> components = new ConcurrentHashMap<>();
    private final Map<String, Long> messageStats = new HashMap<>();
    private final IMessageStrategyChain strategyChain;
    private final IMessageContextBuilder messageContextBuilder;

    public SystemMediator() {
        // 构建策略链
        strategyChain = new MessageStrategyChain();
        messageContextBuilder = new MessageContextBuilder();
    }

    public SystemMediator(IMessageStrategyChain messageHandlerChain) {
        this.strategyChain = messageHandlerChain;
        messageContextBuilder = new MessageContextBuilder();
    }

    @Override
    public void registerComponent(Communicable component) {
        String componentId = component.getComponentId();
        if (components.containsKey(componentId)) {
            log.warn("组件已存在，将覆盖: {}", componentId);
        }
        components.put(componentId, component);
        log.info("注册组件: {}", componentId);
    }

    @Override
    public void unregisterComponent(String componentId) {
        components.remove(componentId);
        log.info("注销组件: {}", componentId);
    }

    @Override
    public void dispatchMessage(IMessage message) {
        updateMessageStats(message.getType().name());
        Communicable receiver = components.get(message.getReceiverId());
        if (receiver == null) {
            sendErrorMessage(message, "接收者不存在 -" + message.getReceiverId());
            return;
        }
        try {
            IMessageContext messageContext = messageContextBuilder.buildFromOriginalMessage(message);
            // 先让消息处理器链处理消息
            strategyChain.process(messageContext);
            // 处理完后再发送消息给接收者
            receiver.receiveMessage(messageContext.getProcessedMessage());
        } catch (MessageHandlingException e) {
            sendErrorMessage(message, "消息处理异常:" + e.getMessage());
        }
    }

    private void sendErrorMessage(IMessage originalMessage, String errorMessageContent) {
        Message errorMessage = new MessageBuilder()
                .fromMessage(originalMessage)
                .from("系统")
                .withContent(errorMessageContent)
                .ofType(MessageType.ERROR)
                .signOperationType("error")
                .build();
        Communicable sender = components.get(originalMessage.getSenderId());
        if (sender != null) {
            sender.receiveMessage(errorMessage);
        }
    }

    private void updateMessageStats(String messageType) {
        messageStats.put(messageType, messageStats.getOrDefault(messageType, 0L) + 1);
    }

    public Map<String, Long> getMessageStats() {
        return new HashMap<>(messageStats);
    }
}
