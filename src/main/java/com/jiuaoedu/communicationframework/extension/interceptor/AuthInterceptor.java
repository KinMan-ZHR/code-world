package com.jiuaoedu.communicationframework.extension.interceptor;

import com.jiuaoedu.communicationframework.api.message.Message;

import java.util.Arrays;
import java.util.List;

public class AuthInterceptor implements MessageInterceptor {
    private static final List<String> ADMIN_COMPONENTS = Arrays.asList("admin", "system");

    @Override
    public boolean preHandle(Message message) {
        // 模拟权限检查
        if (message.getType().name().startsWith("ADMIN_") && 
            !ADMIN_COMPONENTS.contains(message.getSenderId())) {
            System.out.println("权限不足: " + message.getSenderId() + 
                              " 尝试发送管理类消息: " + message.getType());
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(Message message) {
        // 权限检查不需要后置处理
    }
}