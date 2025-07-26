package com.jiuaoedu.framework.communication.extension.strategy;

import com.jiuaoedu.framework.communication.api.message.IMessage;
import com.jiuaoedu.framework.communication.api.message.IMessageBuilder;
import com.jiuaoedu.framework.communication.api.message.handler.IMessageHandler;
import com.jiuaoedu.framework.communication.api.message.handler.strategy.IMessageContext;
import com.jiuaoedu.framework.communication.api.message.MessageType;
import com.jiuaoedu.framework.communication.api.message.handler.strategy.IMessageHandlingStrategy;
import com.jiuaoedu.framework.communication.core.exception.RequestNotFoundException;
import com.jiuaoedu.framework.communication.core.pojo.MessageBuilder;
import com.jiuaoedu.framework.communication.utils.IdGenerator;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class RequestResponseStrategy implements IMessageHandlingStrategy {
    private final Map<String, CompletableFuture<IMessage>> pendingRequests = new ConcurrentHashMap<>();
    private final Map<String, IMessageHandler> requestHandlers = new ConcurrentHashMap<>();
    private final IMessageBuilder messageBuilder;

    public RequestResponseStrategy(IMessageBuilder messageBuilder) {
        this.messageBuilder = messageBuilder;
    }

    public RequestResponseStrategy() {
        this(new MessageBuilder());
    }

    @Override
    public boolean canHandle(IMessageContext messageContext) {
        IMessage message = messageContext.getOriginalMessage();
        return message.getType() == MessageType.REQUEST ||
               message.getType() == MessageType.RESPONSE;
    }

    @Override
    public void handle(IMessageContext messageContext) {
        IMessage message = messageContext.getOriginalMessage();
        if (message.getType() == MessageType.REQUEST) {
            handleRequest(message, messageContext);
        } else if (message.getType() == MessageType.RESPONSE) {
            handleResponse(message, messageContext);
        }
    }

    private void handleRequest(IMessage request, IMessageContext messageContext) {
        String operationType = request.getOperationType();
        // 选择合适的子处理器处理
        IMessageHandler handler = requestHandlers.get(operationType);
        String correlationId = IdGenerator.generateUniqueId();
        String enrichedContent = addCorrelationId(request.getContent(), correlationId);
        IMessage enrichedRequest = messageBuilder
                .fromMessage(request)
                .withContent(enrichedContent)
                .build();
        CompletableFuture<IMessage> future = new CompletableFuture<>();
        pendingRequests.put(correlationId, future);
        handler.handle(enrichedRequest);
    }

    private void handleResponse(IMessage response, IMessageContext messageContext) {
        String correlationId = extractCorrelationId(response.getContent());
        CompletableFuture<IMessage> future = pendingRequests.remove(correlationId);
        
        if (future != null) {
            future.complete(response);
        } else {
            throw new RequestNotFoundException("未找到对应的请求: " + correlationId);
        }
    }

    public void registerRequestHandler(String requestType, IMessageHandler handler) {
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
