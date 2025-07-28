package com.jiuaoedu.framework.communication.extension.strategy;

import com.jiuaoedu.framework.communication.api.message.IMessage;
import com.jiuaoedu.framework.communication.api.message.context.IMessageContext;
import com.jiuaoedu.framework.communication.api.message.context.handler.IMessageContextHandler;
import com.jiuaoedu.framework.communication.api.message.context.IMessageStateMachine;
import com.jiuaoedu.framework.communication.api.message.context.handler.strategy.IMessageHandlingStrategy;
import com.jiuaoedu.framework.communication.api.message.MessageType;
import com.jiuaoedu.framework.communication.api.message.IMessageBuilder;
import com.jiuaoedu.framework.communication.core.pojo.MessageBuilder;
import com.jiuaoedu.framework.communication.core.context.MessageStateMachine;
import com.jiuaoedu.framework.communication.core.exception.MessageHandlingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class RequestResponseStrategy implements IMessageHandlingStrategy {
    private static final Logger logger = LoggerFactory.getLogger(RequestResponseStrategy.class);
    
    private final Map<String, CompletableFuture<IMessage>> pendingRequests = new ConcurrentHashMap<>();
    private final Map<String, IMessageContextHandler> requestHandlers = new ConcurrentHashMap<>();
    private final IMessageBuilder messageBuilder;
    private final IMessageStateMachine stateMachine;

    public RequestResponseStrategy(IMessageBuilder messageBuilder) {
        this.messageBuilder = messageBuilder;
        this.stateMachine = new MessageStateMachine();
    }

    public RequestResponseStrategy() {
        this(new MessageBuilder());
    }

    @Override
    public boolean canHandle(IMessageContext messageContext) {
        IMessage message = messageContext.getOriginalMessage();
        return message.getType() == MessageType.REQUEST ||
               message.getType() == MessageType.RESPONSE ||
               message.getType() == MessageType.ERROR;
    }

    @Override
    public void handle(IMessageContext messageContext) {
        IMessage message = messageContext.getOriginalMessage();
        if (message.getType() == MessageType.REQUEST) {
            handleRequest(messageContext);
        } else if (message.getType() == MessageType.RESPONSE) {
            handleResponse(messageContext);
        } else if (message.getType() == MessageType.ERROR) {
            handleError(messageContext);
        }
    }

    private void handleRequest(IMessageContext messageContext) {
        IMessage request = messageContext.getOriginalMessage();
        String operationType = request.getOperationType();
        if (operationType == null || operationType.isEmpty()) {
            logger.warn("请求消息缺少操作类型");
            return;
        }
        
        // 选择合适的子处理器处理
        IMessageContextHandler handler = requestHandlers.get(operationType);
        if (handler == null){
            // 创建错误响应
            IMessage errorResponse = messageBuilder
                    .fromMessage(request)
                    .ofType(MessageType.ERROR)
                    .withContent("未找到对应的处理器: " + operationType)
                    .build();
            messageContext.setShouldForward(false);
            messageContext.setResult(errorResponse);
            throw new MessageHandlingException("未找到对应的处理器: " + operationType);
        }
        
        // 生成关联ID并增强消息内容
        String enrichedContent = request.getProtocol().returnSignatureAddedContent(request.getContent());
        IMessage enrichedRequest = messageBuilder
                .fromMessage(request)
                .withContent(enrichedContent)
                .build();
        
        // 创建状态跟踪器
        IMessageStateMachine.MessageStateTracker tracker = stateMachine.createRequestTracker(enrichedRequest, 30000);
        if (tracker != null) {
            // 保存跟踪器引用，以便后续处理
            messageContext.setResult(tracker);
        }
        
        // 设置处理后的消息
        messageContext.setProcessedMessage(enrichedRequest);
        
        // 调用处理器
        try {
            handler.handle(messageContext);
        } catch (Exception e) {
            logger.error("处理请求时发生异常: {}", operationType, e);
            // 创建错误响应
            IMessage errorResponse = messageBuilder
                    .fromMessage(enrichedRequest)
                    .ofType(MessageType.ERROR)
                    .withContent("处理请求时发生异常: " + e.getMessage())
                    .build();
            messageContext.setResult(errorResponse);
            throw new MessageHandlingException("处理请求时发生异常", e);
        }
    }

    private void handleResponse(IMessageContext messageContext) {
        IMessage response = messageContext.getOriginalMessage();
        
        // 使用状态机处理响应
        boolean handled = stateMachine.handleResponse(response);
        if (!handled) {
            logger.warn("状态机无法处理响应消息: {}", response.getMessageId());
        }
        
        // 保持向后兼容性
        String correlationId = response.getProtocol().extractSignature(response.getContent());
        CompletableFuture<IMessage> future = pendingRequests.remove(correlationId);
        if (future != null) {
            future.complete(response);
        }
        
        messageContext.setHandled(true);
    }
    
    private void handleError(IMessageContext messageContext) {
        IMessage errorMessage = messageContext.getOriginalMessage();
        
        // 使用状态机处理错误
        boolean handled = stateMachine.handleError(errorMessage);
        if (!handled) {
            logger.warn("状态机无法处理错误消息: {}", errorMessage.getMessageId());
        }
        
        // 保持向后兼容性
        String correlationId = errorMessage.getProtocol().extractSignature(errorMessage.getContent());
        CompletableFuture<IMessage> future = pendingRequests.remove(correlationId);
        if (future != null) {
            future.completeExceptionally(new RuntimeException("收到错误响应: " + errorMessage.getContent()));
        }
        
        messageContext.setHandled(true);
    }

    public void registerRequestHandler(String requestType, IMessageContextHandler handler) {
        requestHandlers.put(requestType, handler);
        logger.info("注册请求处理器: {}", requestType);
    }
    
    /**
     * 获取状态机实例
     */
    public IMessageStateMachine getStateMachine() {
        return stateMachine;
    }
    
    /**
     * 清理资源
     */
    public void shutdown() {
        if (stateMachine instanceof MessageStateMachine) {
            ((MessageStateMachine) stateMachine).shutdown();
        }
        pendingRequests.clear();
        requestHandlers.clear();
        logger.info("RequestResponseStrategy已关闭");
    }
}
