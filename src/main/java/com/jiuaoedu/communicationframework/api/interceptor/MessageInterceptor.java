package com.jiuaoedu.communicationframework.api.interceptor;

import com.jiuaoedu.communicationframework.api.message.Message;

public interface MessageInterceptor {
    boolean preHandle(Message message);
    void postHandle(Message message);
}