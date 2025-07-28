package com.jiuaoedu.framework.communication.api.message.context;

import com.jiuaoedu.framework.communication.api.message.IMessage;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 消息状态机接口
 * 用于管理请求-响应的状态转换和生命周期
 * 
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/26 18:30
 */
public interface IMessageStateMachine {
    
    /**
     * 消息状态枚举
     */
    enum MessageState {
        PENDING,    // 等待响应
        RESPONDED,  // 已收到响应
        TIMEOUT,    // 超时
        ERROR,      // 错误
        CANCELLED   // 已取消
    }
    
    /**
     * 创建新的请求状态跟踪
     * 
     * @param request 请求消息
     * @param timeout 超时时间（毫秒）
     * @return 状态跟踪器
     */
    MessageStateTracker createRequestTracker(IMessage request, long timeout);
    
    /**
     * 处理响应消息
     * 
     * @param response 响应消息
     * @return 是否成功处理
     */
    boolean handleResponse(IMessage response);
    
    /**
     * 处理错误消息
     * 
     * @param errorMessage 错误消息
     * @return 是否成功处理
     */
    boolean handleError(IMessage errorMessage);
    
    /**
     * 清理过期的状态跟踪
     */
    void cleanupExpiredTrackers();
    
    /**
     * 获取当前活跃的请求数量
     * 
     * @return 活跃请求数量
     */
    int getActiveRequestCount();
    
    /**
     * 消息状态跟踪器
     */
    interface MessageStateTracker {
        
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
}