package com.jiuaoedu.framework.communication.api.message.context;

/**
  * 消息状态枚举
  */
public enum MessageState {
    PENDING,    // 等待响应
    RESPONDED,  // 已收到响应
    TIMEOUT,    // 超时
    ERROR,      // 错误
    CANCELLED   // 已取消
}