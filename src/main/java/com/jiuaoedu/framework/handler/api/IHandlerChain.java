package com.jiuaoedu.framework.handler.api;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/25 17:08
 */
public interface IHandlerChain<T> {
    void addNextHandler(IHandler<T> handler);
    void process(T t);
}
