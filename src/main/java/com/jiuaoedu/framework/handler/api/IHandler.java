package com.jiuaoedu.framework.handler.api;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/25 17:06
 */
public interface IHandler<T> {
    boolean canHandle(T t);
    void handle(T t);
}
