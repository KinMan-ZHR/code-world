package com.jiuaoedu.communicationframework.extension.interceptor;

import com.jiuaoedu.communicationframework.api.message.Message;

public interface MessageInterceptor {
    boolean preHandle(Message message);
    void postHandle(Message message);
}