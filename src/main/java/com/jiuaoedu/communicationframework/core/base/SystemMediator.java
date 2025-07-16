package com.jiuaoedu.communicationframework.core.base;

import com.jiuaoedu.communicationframework.api.communicator.Communicable;
import com.jiuaoedu.communicationframework.api.mediator.Mediator;
import com.jiuaoedu.communicationframework.api.message.Message;
import com.jiuaoedu.communicationframework.api.message.MessageType;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SystemMediator implements Mediator {
    private final Map<String, Communicable> components = new ConcurrentHashMap<>();
    private final Map<String, Long> messageStats = new HashMap<>();

    @Override
    public void registerComponent(Communicable component) {
        components.put(component.getComponentId(), component);
        System.out.println("注册组件: " + component.getComponentId());
    }

    @Override
    public void unregisterComponent(String componentId) {
        components.remove(componentId);
        System.out.println("注销组件: " + componentId);
    }

    @Override
    public void dispatchMessage(Message message) {
        updateMessageStats(message.getType().name());
        
        Communicable receiver = components.get(message.getReceiverId());
        if (receiver == null) {
            System.err.println("错误: 接收者不存在 - " + message.getReceiverId());
            sendErrorMessage(message);
            return;
        }
        
        try {
            receiver.receiveMessage(message);
        } catch (Exception e) {
            System.err.println("消息处理异常: " + e.getMessage());
            sendErrorMessage(message);
        }
    }

    private void sendErrorMessage(Message originalMessage) {
        Message errorMessage = new Message(
            "系统",
            originalMessage.getSenderId(),
            "处理消息失败: " + originalMessage.getContent(),
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
