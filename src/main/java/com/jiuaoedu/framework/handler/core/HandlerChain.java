package com.jiuaoedu.framework.handler.core;

import com.jiuaoedu.framework.handler.api.IHandler;
import com.jiuaoedu.framework.handler.api.IHandlerChain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/25 18:10
 */
public class HandlerChain<T> implements IHandlerChain<T> {

    private final List<IHandler<T>> handlers = new ArrayList<>();
    private int currentIndex = 0;

    @Override
    public void addNextHandler(IHandler<T> handler) {
        handlers.add(handler);
    }

    @Override
    public void process(T t) {
        if (currentIndex < handlers.size()) {
            IHandler<T> handler = handlers.get(currentIndex++);
            if (handler.canHandle(t)) {
                handler.handle(t);
            }
            // 无论当前处理器是否处理，都继续传递给下一个处理器
            process(t);
        }
    }
}
