package com.jiuaoedu.framework.communication.api.interceptor;

import com.jiuaoedu.framework.communication.api.message.IMessage;

public interface IMessageInterceptor {
    boolean preHandle(IMessage message);
    void postHandle(IMessage message);
}