package com.jiuaoedu.framework.communication.api.message.context;

import com.jiuaoedu.framework.communication.api.message.IMessage;

/**
 * 消息状态机接口
 * 用于管理请求-响应的状态转换和生命周期
 * 
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/26 18:30
 */
public interface IMessageStateMachine {

    /**
     * 创建新的请求状态跟踪
     * 
     * @param request 请求消息
     * @param timeout 超时时间（毫秒）
     * @return 状态跟踪器
     */
    IMessageStateTracker createRequestTracker(IMessage request, long timeout);
    
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
}