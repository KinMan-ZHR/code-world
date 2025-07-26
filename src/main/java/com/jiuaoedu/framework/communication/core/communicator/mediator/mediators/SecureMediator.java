package com.jiuaoedu.framework.communication.core.communicator.mediator.mediators;

import com.jiuaoedu.framework.communication.api.communicator.Communicable;
import com.jiuaoedu.framework.communication.api.communicator.type.mediator.ISecureMediator;
import com.jiuaoedu.framework.communication.api.communicator.type.mediator.IMediator;
import com.jiuaoedu.framework.communication.api.message.IMessage;
import com.jiuaoedu.framework.communication.extension.interceptor.AuthInterceptor;
import com.jiuaoedu.framework.communication.api.interceptor.IMessageInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SecureMediator implements ISecureMediator {
    private static final Logger log = LoggerFactory.getLogger(SecureMediator.class);
    private final IMediator delegate;
    private final List<IMessageInterceptor> interceptors = new ArrayList<>();

    public SecureMediator(IMediator delegate) {
        this.delegate = delegate;
        // 添加默认安全拦截器
        interceptors.add(new AuthInterceptor());
    }

    public void addInterceptor(IMessageInterceptor interceptor) {
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
    public void dispatchMessage(IMessage message) {
        for (IMessageInterceptor interceptor : interceptors) {
            if (!interceptor.preHandle(message)) {
                log.warn("消息被拦截: {}", message);
                return;
            }
        }
        
        delegate.dispatchMessage(message);
        
        for (IMessageInterceptor interceptor : interceptors) {
            interceptor.postHandle(message);
        }
    }
}