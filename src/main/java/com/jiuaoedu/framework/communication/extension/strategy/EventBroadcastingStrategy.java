package com.jiuaoedu.framework.communication.extension.strategy;

import com.jiuaoedu.framework.communication.api.message.IMessage;
import com.jiuaoedu.framework.communication.api.message.handler.IMessageHandler;
import com.jiuaoedu.framework.communication.api.message.handler.strategy.IMessageContext;
import com.jiuaoedu.framework.communication.api.message.handler.strategy.IMessageHandlingStrategy;
import com.jiuaoedu.framework.communication.core.pojo.Message;
import com.jiuaoedu.framework.communication.api.message.MessageType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventBroadcastingStrategy implements IMessageHandlingStrategy {
    private final Map<String, List<IMessageHandler>> eventListeners = new ConcurrentHashMap<>();

    @Override
    public boolean canHandle(IMessageContext messageContext) {
        return messageContext.getOriginalMessage().getType() == MessageType.EVENT;
    }

    @Override
    public void handle(IMessageContext messageContext) {
        IMessage message = messageContext.getOriginalMessage();
        String eventType = extractEventType(message.getContent());
        List<IMessageHandler> handlers = eventListeners.getOrDefault(eventType, new ArrayList<>());
        
        for (IMessageHandler handler : handlers) {
            handler.handle(message);
        }
    }

    public void addEventListener(String eventType, IMessageHandler listener) {
        eventListeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    public void removeEventListener(String eventType, IMessageHandler listener) {
        List<IMessageHandler> handlers = eventListeners.get(eventType);
        if (handlers != null) {
            handlers.remove(listener);
        }
    }

    private String extractEventType(String content) {
        // 简单实现，实际应根据协议解析
        return content.split(":")[0];
    }
}
