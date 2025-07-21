package com.jiuaoedu.communicationframework.core.mediator;

import com.jiuaoedu.communicationframework.api.communicator.Communicable;
import com.jiuaoedu.communicationframework.api.mediator.Mediator;
import com.jiuaoedu.communicationframework.api.message.Message;
import com.jiuaoedu.communicationframework.api.message.MessageType;
import com.jiuaoedu.communicationframework.core.exception.MessageHandlingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SystemMediator implements Mediator {
    private static final Logger log = LoggerFactory.getLogger(SystemMediator.class);
    private final Map<String, Communicable> components = new ConcurrentHashMap<>();
    private final Map<String, Long> messageStats = new HashMap<>();

    @Override
    public void registerComponent(Communicable component) {
        components.put(component.getComponentId(), component);
       log.info("注册组件: {}", component.getComponentId());
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
            receiver.receiveMessage(message);
        } catch (MessageHandlingException e) {
            log.error("消息处理异常: {}", e.getMessage());
            sendErrorMessage(message, "消息处理异常");
        }
    }

    private void sendErrorMessage(Message originalMessage, String errorMessageContent) {
        Message errorMessage = new Message(
            "系统",
            originalMessage.getSenderId(),
            errorMessageContent + originalMessage.getContent(),
            MessageType.ERROR
        );
        
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
