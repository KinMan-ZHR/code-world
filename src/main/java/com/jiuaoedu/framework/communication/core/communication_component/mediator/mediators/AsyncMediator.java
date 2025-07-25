package com.jiuaoedu.framework.communication.core.communication_component.mediator.mediators;

import com.jiuaoedu.framework.communication.api.communicator.Communicable;
import com.jiuaoedu.framework.communication.api.communicator.type.mediator.Mediator;
import com.jiuaoedu.framework.communication.api.message.Message;
import com.jiuaoedu.framework.communication.utils.ThreadPoolUtil;

import java.util.concurrent.ExecutorService;

public class AsyncMediator implements Mediator {
    private final Mediator delegate;
    private final ExecutorService threadPool;

    public AsyncMediator(Mediator delegate) {
        this.delegate = delegate;
        this.threadPool = ThreadPoolUtil.createFixedThreadPool(10, "async-mediator",20);
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
        threadPool.submit(() -> delegate.dispatchMessage(message));
    }

    public void shutdown() {
        threadPool.shutdown();
    }
}
