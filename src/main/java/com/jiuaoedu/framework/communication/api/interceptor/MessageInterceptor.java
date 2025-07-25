package com.jiuaoedu.framework.communication.api.interceptor;

import com.jiuaoedu.framework.communication.api.message.Message;

public interface MessageInterceptor {
    boolean preHandle(Message message);
    void postHandle(Message message);
}