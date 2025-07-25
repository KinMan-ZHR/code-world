package com.jiuaoedu.framework.communication.extension.strategy;

import com.jiuaoedu.framework.communication.api.message.Message;
import com.jiuaoedu.framework.communication.api.message.MessageType;
import com.jiuaoedu.framework.handler.api.IHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventBroadcastingStrategy implements IHandler<Message> {
    private final Map<String, List<IHandler<Message>>> eventListeners = new ConcurrentHashMap<>();

    @Override
    public boolean canHandle(Message message) {
        return message.getType() == MessageType.EVENT;
    }

    @Override
    public void handle(Message message) {
        String eventType = extractEventType(message.getContent());
        List<IHandler<Message>> handlers = eventListeners.getOrDefault(eventType, new ArrayList<>());
        
        for (IHandler<Message> handler : handlers) {
            handler.handle(message);
        }
    }

    public void addEventListener(String eventType, IHandler<Message> listener) {
        eventListeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    public void removeEventListener(String eventType, IHandler<Message> listener) {
        List<IHandler<Message>> handlers = eventListeners.get(eventType);
        if (handlers != null) {
            handlers.remove(listener);
        }
    }

    private String extractEventType(String content) {
        // 简单实现，实际应根据协议解析
        return content.split(":")[0];
    }
}
