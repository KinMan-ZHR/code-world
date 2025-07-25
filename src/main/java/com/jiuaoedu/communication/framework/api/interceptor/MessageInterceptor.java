package com.jiuaoedu.communication.framework.api.interceptor;

import com.jiuaoedu.communication.framework.api.message.Message;

public interface MessageInterceptor {
    boolean preHandle(Message message);
    void postHandle(Message message);
}