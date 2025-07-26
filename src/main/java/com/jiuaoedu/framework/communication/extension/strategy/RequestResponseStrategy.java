package com.jiuaoedu.framework.communication.extension.strategy;

import com.jiuaoedu.framework.communication.api.message.IMessage;
import com.jiuaoedu.framework.communication.api.message.IMessageBuilder;
import com.jiuaoedu.framework.communication.api.message.context.handler.IMessageContextHandler;
import com.jiuaoedu.framework.communication.api.message.context.IMessageContext;
import com.jiuaoedu.framework.communication.api.message.MessageType;
import com.jiuaoedu.framework.communication.api.message.context.handler.strategy.IMessageHandlingStrategy;
import com.jiuaoedu.framework.communication.core.exception.MessageHandlingException;
import com.jiuaoedu.framework.communication.core.exception.RequestNotFoundException;
import com.jiuaoedu.framework.communication.core.pojo.MessageBuilder;
import com.jiuaoedu.framework.communication.utils.IdGenerator;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class RequestResponseStrategy implements IMessageHandlingStrategy {
    private final Map<String, CompletableFuture<IMessage>> pendingRequests = new ConcurrentHashMap<>();
    private final Map<String, IMessageContextHandler> requestHandlers = new ConcurrentHashMap<>();
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
            handleRequest(messageContext);
        } else if (message.getType() == MessageType.RESPONSE) {
            handleResponse(messageContext);
        }
    }

    private void handleRequest(IMessageContext messageContext) {
        IMessage request = messageContext.getOriginalMessage();
        String operationType = request.getOperationType();
        if (operationType == null || operationType.isEmpty()) {
            // 无需处理
            return;
        }
        // 选择合适的子处理器处理
        IMessageContextHandler handler = requestHandlers.get(operationType);
        if (handler == null){
            // 之后添加进入系统日志记录中，督促开发者开发对应子处理器
            messageContext.setShouldForward(false);
            messageContext.setResult(new MessageBuilder()
                    .fromMessage(request)
                    .ofType(MessageType.ERROR)
                    .withContent("未找到对应的处理器: " + operationType)
                    .build());
            throw new MessageHandlingException("未找到对应的处理器: " + operationType);
        }
        String correlationId = IdGenerator.generateUniqueId();
        String enrichedContent = request.getProtocol().generateContentWithCorrelationId(request.getContent(), correlationId);
        IMessage enrichedRequest = messageBuilder
                .fromMessage(request)
                .withContent(enrichedContent)
                .build();
        CompletableFuture<IMessage> future = new CompletableFuture<>();
        pendingRequests.put(correlationId, future);
        messageContext.setProcessedMessage(enrichedRequest);
        handler.handle(messageContext);
    }

    private void handleResponse(IMessageContext messageContext) {
        IMessage response = messageContext.getOriginalMessage();
        String correlationId = response.getProtocol().extractCorrelationId(response.getContent());
        CompletableFuture<IMessage> future = pendingRequests.remove(correlationId);
        if (future != null) {
            future.complete(response);
        } else {
            throw new RequestNotFoundException("未找到对应的请求: " + correlationId);
        }
    }

    public void registerRequestHandler(String requestType, IMessageContextHandler handler) {
        requestHandlers.put(requestType, handler);
    }
}
