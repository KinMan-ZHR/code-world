package com.jiuaoedu.communicationframework.extension.strategy;

import com.jiuaoedu.communicationframework.api.communicator.MessageHandler;
import com.jiuaoedu.communicationframework.api.message.Message;
import com.jiuaoedu.communicationframework.api.message.MessageType;
import com.jiuaoedu.communicationframework.core.base.BaseMessageHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBroadcastingStrategy extends BaseMessageHandler {
    private final Map<String, List<MessageHandler>> eventListeners = new HashMap<>();

    @Override
    protected boolean canHandle(Message message) {
        return message.getType() == MessageType.EVENT;
    }

    @Override
    protected void doHandle(Message message) {
        String eventType = extractEventType(message.getContent());
        List<MessageHandler> handlers = eventListeners.getOrDefault(eventType, new ArrayList<>());
        
        for (MessageHandler handler : handlers) {
            handler.handleMessage(message);
        }
    }

    public void addEventListener(String eventType, MessageHandler listener) {
        eventListeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
    }

    public void removeEventListener(String eventType, MessageHandler listener) {
        List<MessageHandler> handlers = eventListeners.get(eventType);
        if (handlers != null) {
            handlers.remove(listener);
        }
    }

    private String extractEventType(String content) {
        // 简单实现，实际应根据协议解析
        return content.split(":")[0];
    }
}
