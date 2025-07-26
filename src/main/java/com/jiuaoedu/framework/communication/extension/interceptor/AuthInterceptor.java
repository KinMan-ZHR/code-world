package com.jiuaoedu.framework.communication.extension.interceptor;

import com.jiuaoedu.framework.communication.api.interceptor.IMessageInterceptor;
import com.jiuaoedu.framework.communication.api.message.IMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class AuthInterceptor implements IMessageInterceptor {
    private static final List<String> ADMIN_COMPONENTS = Arrays.asList("admin", "system");
    private static final Logger log = LoggerFactory.getLogger(AuthInterceptor.class);

    @Override
    public boolean preHandle(IMessage message) {
        // 模拟权限检查
        if (message.getType().name().startsWith("ADMIN_") && 
            !ADMIN_COMPONENTS.contains(message.getSenderId())) {
            AuthInterceptor.log.warn("权限不足: {} 尝试发送管理类消息: {}", message.getSenderId(),  message.getType());
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(IMessage message) {
        // 权限检查不需要后置处理
    }
}