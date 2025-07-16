package com.jiuaoedu.communicationframework.core.base;

import com.jiuaoedu.communicationframework.api.communicator.Communicable;
import com.jiuaoedu.communicationframework.api.mediator.Mediator;
import com.jiuaoedu.communicationframework.api.message.Message;
import com.jiuaoedu.communicationframework.utils.ThreadPoolUtil;

import java.util.concurrent.ExecutorService;

public class AsyncMediator implements Mediator {
    private final Mediator delegate;
    private final ExecutorService threadPool;

    public AsyncMediator(Mediator delegate) {
        this.delegate = delegate;
        this.threadPool = ThreadPoolUtil.createFixedThreadPool(10, "async-mediator");
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
