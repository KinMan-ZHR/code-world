package com.jiuaoedu.communicationframework.extension.strategy;


import com.jiuaoedu.communicationframework.api.communicator.MessageHandler;
import com.jiuaoedu.communicationframework.api.message.Message;
import com.jiuaoedu.communicationframework.api.message.MessageType;
import com.jiuaoedu.communicationframework.core.base.BaseMessageHandler;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class RequestResponseStrategy extends BaseMessageHandler {
    private final Map<String, CompletableFuture<Message>> pendingRequests = new ConcurrentHashMap<>();
    private final Map<String, MessageHandler> requestHandlers = new HashMap<>();

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
        String requestType = extractRequestType(request.getContent());
        MessageHandler handler = requestHandlers.get(requestType);
        
        if (handler != null) {
            handler.handleMessage(request);
        } else {
            System.err.println("未找到请求处理器: " + requestType);
            sendErrorResponse(request, "未找到处理器");
        }
    }

    public void handleResponse(Message response) {
        String correlationId = extractCorrelationId(response.getContent());
        CompletableFuture<Message> future = pendingRequests.remove(correlationId);
        
        if (future != null) {
            future.complete(response);
        } else {
            System.err.println("未找到对应的请求: " + correlationId);
        }
    }

    public CompletableFuture<Message> sendRequest(Message request) {
        String correlationId = UUID.randomUUID().toString();
        String enrichedContent = addCorrelationId(request.getContent(), correlationId);
        
        Message enrichedRequest = new Message(
            request.getSenderId(),
            request.getReceiverId(),
            enrichedContent,
            MessageType.REQUEST
        );
        
        CompletableFuture<Message> future = new CompletableFuture<>();
        pendingRequests.put(correlationId, future);
        
        // 假设这里有一个sender可以发送消息
        // sender.sendMessage(enrichedRequest);
        
        return future;
    }

    public void registerRequestHandler(String requestType, MessageHandler handler) {
        requestHandlers.put(requestType, handler);
    }

    private String extractRequestType(String content) {
        // 简单实现，实际应根据协议解析
        return content.split(":")[0];
    }

    private String extractCorrelationId(String content) {
        // 简单实现，实际应根据协议解析
        return content.split(";")[0].split("=")[1];
    }

    private String addCorrelationId(String content, String correlationId) {
        return "correlationId=" + correlationId + ";" + content;
    }

    private void sendErrorResponse(Message originalRequest, String errorMessage) {
        Message response = new Message(
            originalRequest.getReceiverId(),
            originalRequest.getSenderId(),
            "ERROR: " + errorMessage,
            MessageType.RESPONSE
        );
        
        // 假设这里有一个sender可以发送消息
        // sender.sendMessage(response);
    }
}
