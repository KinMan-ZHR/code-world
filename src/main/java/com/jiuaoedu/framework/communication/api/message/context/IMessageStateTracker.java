package com.jiuaoedu.framework.communication.api.message.context;

import com.jiuaoedu.framework.communication.api.message.IMessage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
     * 消息状态跟踪器
     */
public interface IMessageStateTracker {

    /**
     * 获取关联ID
     */
    String getCorrelationId();

    /**
     * 获取当前状态
     */
    MessageState getState();

    /**
     * 获取原始请求
     */
    IMessage getRequest();

    /**
     * 获取响应（如果已收到）
     */
    IMessage getResponse();

    /**
     * 获取错误消息（如果有）
     */
    IMessage getError();

    /**
     * 等待响应
     *
     * @param timeout 超时时间
     * @param unit 时间单位
     * @return 响应消息的Future
     */
    CompletableFuture<IMessage> waitForResponse(long timeout, TimeUnit unit);

    /**
     * 取消请求
     */
    void cancel();

    /**
     * 检查是否已超时
     */
    boolean isTimeout();

    /**
     * 获取创建时间
     */
    long getCreatedTime();

    /**
     * 获取最后更新时间
     */
    long getLastUpdatedTime();
}