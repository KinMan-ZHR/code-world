package com.jiuaoedu.communicationframework.core.mediator;

import com.jiuaoedu.communicationframework.api.communicator.Communicable;
import com.jiuaoedu.communicationframework.api.mediator.ISecureMediator;
import com.jiuaoedu.communicationframework.api.mediator.Mediator;
import com.jiuaoedu.communicationframework.api.message.Message;
import com.jiuaoedu.communicationframework.extension.interceptor.AuthInterceptor;
import com.jiuaoedu.communicationframework.api.interceptor.MessageInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SecureMediator implements ISecureMediator {
    private static final Logger log = LoggerFactory.getLogger(SecureMediator.class);
    private final Mediator delegate;
    private final List<MessageInterceptor> interceptors = new ArrayList<>();

    public SecureMediator(Mediator delegate) {
        this.delegate = delegate;
        // 添加默认安全拦截器
        interceptors.add(new AuthInterceptor());
    }

    public void addInterceptor(MessageInterceptor interceptor) {
        interceptors.add(interceptor);
    }

    @Override
    public void registerComponent(Communicable component) {
        delegate.registerComponent(component);
    }

    @Override
    public void unregisterComponent(String componentId) {
        delegate.unregisterComponent(componentId);
    }

    @Override
    public void dispatchMessage(Message message) {
        for (MessageInterceptor interceptor : interceptors) {
            if (!interceptor.preHandle(message)) {
                log.warn("消息被拦截: {}", message);
                return;
            }
        }
        
        delegate.dispatchMessage(message);
        
        for (MessageInterceptor interceptor : interceptors) {
            interceptor.postHandle(message);
        }
    }
}