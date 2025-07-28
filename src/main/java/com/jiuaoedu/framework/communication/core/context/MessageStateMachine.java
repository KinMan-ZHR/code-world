package com.jiuaoedu.framework.communication.core.context;

import com.jiuaoedu.framework.communication.api.message.IMessage;
import com.jiuaoedu.framework.communication.api.message.MessageType;
import com.jiuaoedu.framework.communication.api.message.context.IMessageStateMachine;
import com.jiuaoedu.framework.communication.api.message.context.IMessageStateTracker;
import com.jiuaoedu.framework.communication.api.message.context.MessageState;
import com.jiuaoedu.framework.communication.core.protocol.CorrelationMessageProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消息状态机实现
 * 管理请求-响应的完整生命周期
 *
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/26 18:35
 */
public class MessageStateMachine implements IMessageStateMachine {

    private static final Logger logger = LoggerFactory.getLogger(MessageStateMachine.class);
    private static final long DEFAULT_TIMEOUT = 30000; // 30秒默认超时
    private static final long CLEANUP_INTERVAL = 60000; // 1分钟清理间隔

    private final Map<String, MessageStateTrackerImpl> trackers = new ConcurrentHashMap<>();
    private final ScheduledExecutorService cleanupScheduler;
    private final AtomicInteger activeRequestCount = new AtomicInteger(0);
    private final CorrelationMessageProtocol protocol = new CorrelationMessageProtocol();

    public MessageStateMachine() {
        this.cleanupScheduler = Executors.newSingleThreadScheduledExecutor(r -> {
            Thread t = new Thread(r, "message-state-cleanup");
            t.setDaemon(true);
            return t;
        });

        // 启动定期清理任务
        this.cleanupScheduler.scheduleAtFixedRate(
                this::cleanupExpiredTrackers,
                CLEANUP_INTERVAL,
                CLEANUP_INTERVAL,
                TimeUnit.MILLISECONDS
        );
    }

    @Override
    public IMessageStateTracker createRequestTracker(IMessage request, long timeout) {
        if (request.getType() != MessageType.REQUEST) {
            throw new IllegalArgumentException("只能为REQUEST类型的消息创建跟踪器");
        }

        String correlationId = protocol.extractValueFromSignature(protocol.extractSignature(request.getContent()));
        if (correlationId == null) {
            logger.warn("请求消息缺少关联ID，无法创建跟踪器");
            return null;
        }

        MessageStateTrackerImpl tracker = new MessageStateTrackerImpl(request, correlationId, timeout);
        trackers.put(correlationId, tracker);
        activeRequestCount.incrementAndGet();

        logger.debug("创建请求跟踪器: {}", correlationId);
        return tracker;
    }

    @Override
    public boolean handleResponse(IMessage response) {
        if (response.getType() != MessageType.RESPONSE) {
            return false;
        }

        String correlationId = protocol.extractValueFromSignature(protocol.extractSignature(response.getContent()));
        if (correlationId == null) {
            logger.warn("响应消息缺少关联ID");
            return false;
        }

        MessageStateTrackerImpl tracker = trackers.get(correlationId);
        if (tracker == null) {
            logger.warn("未找到对应的请求跟踪器: {}", correlationId);
            return false;
        }

        boolean success = tracker.handleResponse(response);
        if (success) {
            activeRequestCount.decrementAndGet();
        }

        return success;
    }

    @Override
    public boolean handleError(IMessage errorMessage) {
        if (errorMessage.getType() != MessageType.ERROR) {
            return false;
        }

        String correlationId = protocol.extractValueFromSignature(protocol.extractSignature(errorMessage.getContent()));
        if (correlationId == null) {
            logger.warn("错误消息缺少关联ID");
            return false;
        }

        MessageStateTrackerImpl tracker = trackers.get(correlationId);
        if (tracker == null) {
            logger.warn("未找到对应的请求跟踪器: {}", correlationId);
            return false;
        }

        boolean success = tracker.handleError(errorMessage);
        if (success) {
            activeRequestCount.decrementAndGet();
        }

        return success;
    }

    @Override
    public void cleanupExpiredTrackers() {
        long now = System.currentTimeMillis();
        int cleanedCount = 0;

        for (Map.Entry<String, MessageStateTrackerImpl> entry : trackers.entrySet()) {
            MessageStateTrackerImpl tracker = entry.getValue();
            if (tracker.isExpired(now)) {
                trackers.remove(entry.getKey());
                tracker.cancel();
                activeRequestCount.decrementAndGet();
                cleanedCount++;
            }
        }

        if (cleanedCount > 0) {
            logger.info("清理了 {} 个过期的请求跟踪器", cleanedCount);
        }
    }

    @Override
    public int getActiveRequestCount() {
        return activeRequestCount.get();
    }

    /**
     * 关闭状态机，清理资源
     */
    public void shutdown() {
        cleanupScheduler.shutdown();
        trackers.clear();
        activeRequestCount.set(0);
        logger.info("消息状态机已关闭");
    }

    /**
     * 消息状态跟踪器实现
     */
    private static class MessageStateTrackerImpl implements IMessageStateTracker {

        private final IMessage request;
        private final String correlationId;
        private final long timeout;
        private final long createdTime;
        private final CompletableFuture<IMessage> responseFuture;

        private volatile MessageState state = MessageState.PENDING;
        private volatile IMessage response;
        private volatile IMessage error;
        private volatile long lastUpdatedTime;

        public MessageStateTrackerImpl(IMessage request, String correlationId, long timeout) {
            this.request = request;
            this.correlationId = correlationId;
            this.timeout = timeout > 0 ? timeout : DEFAULT_TIMEOUT;
            this.createdTime = System.currentTimeMillis();
            this.lastUpdatedTime = this.createdTime;
            this.responseFuture = new CompletableFuture<>();
        }

        @Override
        public String getCorrelationId() {
            return correlationId;
        }

        @Override
        public MessageState getState() {
            return state;
        }

        @Override
        public IMessage getRequest() {
            return request;
        }

        @Override
        public IMessage getResponse() {
            return response;
        }

        @Override
        public IMessage getError() {
            return error;
        }

        @Override
        public CompletableFuture<IMessage> waitForResponse(long timeout, TimeUnit unit) {
            CompletableFuture<IMessage> timeoutFuture = new CompletableFuture<>();
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            
            scheduler.schedule(() -> {
                if (!responseFuture.isDone()) {
                    timeoutFuture.completeExceptionally(new TimeoutException("请求超时"));
                }
                scheduler.shutdown();
            }, timeout, unit);
            
            return responseFuture.applyToEither(timeoutFuture, response -> response);
        }

        @Override
        public void cancel() {
            if (state == MessageState.PENDING) {
                state = MessageState.CANCELLED;
                responseFuture.cancel(true);
                lastUpdatedTime = System.currentTimeMillis();
                logger.debug("请求已取消: {}", correlationId);
            }
        }

        @Override
        public boolean isTimeout() {
            return System.currentTimeMillis() - createdTime > timeout;
        }

        @Override
        public long getCreatedTime() {
            return createdTime;
        }

        @Override
        public long getLastUpdatedTime() {
            return lastUpdatedTime;
        }

        public boolean handleResponse(IMessage response) {
            if (state != MessageState.PENDING) {
                logger.warn("请求状态不是PENDING，无法处理响应: {}", correlationId);
                return false;
            }

            this.response = response;
            this.state = MessageState.RESPONDED;
            this.lastUpdatedTime = System.currentTimeMillis();
            this.responseFuture.complete(response);

            logger.debug("请求收到响应: {}", correlationId);
            return true;
        }

        public boolean handleError(IMessage errorMessage) {
            if (state != MessageState.PENDING) {
                logger.warn("请求状态不是PENDING，无法处理错误: {}", correlationId);
                return false;
            }

            this.error = errorMessage;
            this.state = MessageState.ERROR;
            this.lastUpdatedTime = System.currentTimeMillis();
            this.responseFuture.completeExceptionally(
                    new RuntimeException("请求处理失败: " + errorMessage.getContent())
            );

            logger.debug("请求收到错误响应: {}", correlationId);
            return true;
        }

        public boolean isExpired(long currentTime) {
            return currentTime - createdTime > timeout;
        }
    }
}