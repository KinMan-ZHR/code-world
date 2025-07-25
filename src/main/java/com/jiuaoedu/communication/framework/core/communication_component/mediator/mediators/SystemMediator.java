package com.jiuaoedu.communication.framework.core.communication_component.mediator.mediators;

import com.jiuaoedu.communication.framework.api.communicator.Communicable;
import com.jiuaoedu.communication.framework.api.communicator.type.mediator.Mediator;
import com.jiuaoedu.communication.framework.api.message.Message;
import com.jiuaoedu.communication.framework.api.message.MessageBuilder;
import com.jiuaoedu.communication.framework.core.message_handler.BaseMessageHandler;
import com.jiuaoedu.communication.framework.core.exception.MessageHandlingException;
import com.jiuaoedu.communication.framework.extension.strategy.EventBroadcastingStrategy;
import com.jiuaoedu.communication.framework.extension.strategy.RequestResponseStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SystemMediator implements Mediator {
    private static final Logger log = LoggerFactory.getLogger(SystemMediator.class);
    private final Map<String, Communicable> components = new ConcurrentHashMap<>();
    private final Map<String, Long> messageStats = new HashMap<>();
    private final BaseMessageHandler strategyChain;

    public SystemMediator() {
        // 构建策略链
        RequestResponseStrategy requestResponseStrategy = new RequestResponseStrategy();
        EventBroadcastingStrategy eventBroadcastingStrategy = new EventBroadcastingStrategy();
        strategyChain = requestResponseStrategy.setNextHandler(eventBroadcastingStrategy);
    }

    public SystemMediator(BaseMessageHandler messageHandlerChain) {
        this.strategyChain = messageHandlerChain;
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
    public void dispatchMessage(Message message) {
        updateMessageStats(message.getType().name());
        
        Communicable receiver = components.get(message.getReceiverId());
        if (receiver == null) {
            log.error("错误: 接收者不存在 - {}", message.getReceiverId());
            sendErrorMessage(message, "接收者不存在");
            return;
        }
        try {
            // 先让消息处理器链处理消息
            strategyChain.handleMessage(message);
            // 处理完后再发送消息给接收者
            receiver.receiveMessage(message);
        } catch (MessageHandlingException e) {
            log.error("消息处理异常: {}", e.getMessage());
            sendErrorMessage(message, "消息处理异常");
        }
    }

    private void sendErrorMessage(Message originalMessage, String errorMessageContent) {
        Message errorMessage = new MessageBuilder()
                .fromMessage(originalMessage)
                .from("系统")
                .withContent(errorMessageContent + originalMessage.getContent())
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
