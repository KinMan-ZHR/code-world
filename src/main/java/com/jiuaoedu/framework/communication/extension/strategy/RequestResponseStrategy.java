package com.jiuaoedu.framework.communication.extension.strategy;

import com.jiuaoedu.framework.communication.api.message.handler.MessageHandler;
import com.jiuaoedu.framework.communication.api.message.Message;
import com.jiuaoedu.framework.communication.api.message.MessageBuilder;
import com.jiuaoedu.framework.communication.api.message.MessageType;
import com.jiuaoedu.framework.communication.core.message_handler.BaseMessageHandler;
import com.jiuaoedu.framework.communication.core.exception.RequestNotFoundException;
import com.jiuaoedu.framework.communication.utils.IdGenerator;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class RequestResponseStrategy extends BaseMessageHandler {
    private final Map<String, CompletableFuture<Message>> pendingRequests = new ConcurrentHashMap<>();
    private final Map<String, MessageHandler> requestHandlers = new ConcurrentHashMap<>();

    @Override
    protected boolean canHandle(Message message) {
        return message.getType() == MessageType.REQUEST ||
               message.getType() == MessageType.RESPONSE;
    }

    @Override
    protected void doHandle(Message message) {
        if (message.getType() == MessageType.REQUEST) {
            handleRequest(message);
        } else if (message.getType() == MessageType.RESPONSE) {
            handleResponse(message);
        }
    }

    public void handleRequest(Message request) {
        String operationType = request.getOperationType();
        // 选择合适的子处理器处理
        MessageHandler handler = requestHandlers.get(operationType);
        String correlationId = IdGenerator.generateUniqueId();
        String enrichedContent = addCorrelationId(request.getContent(), correlationId);
        Message enrichedRequest = new MessageBuilder()
                .fromMessage(request)
                .withContent(enrichedContent)
                .build();
        CompletableFuture<Message> future = new CompletableFuture<>();
        pendingRequests.put(correlationId, future);
        handler.handleMessage(enrichedRequest);
    }

    public void handleResponse(Message response) {
        String correlationId = extractCorrelationId(response.getContent());
        CompletableFuture<Message> future = pendingRequests.remove(correlationId);
        
        if (future != null) {
            future.complete(response);
        } else {
            throw new RequestNotFoundException("未找到对应的请求: " + correlationId);
        }
    }

    public void registerRequestHandler(String requestType, MessageHandler handler) {
        requestHandlers.put(requestType, handler);
    }

    private String addCorrelationId(String content, String correlationId) {
        return "correlationId=" + correlationId + ";" + content;
    }

    private String extractCorrelationId(String content) {
        // 简单实现，实际应根据协议解析
        return content.split(";")[0].split("=")[1];
    }
}
