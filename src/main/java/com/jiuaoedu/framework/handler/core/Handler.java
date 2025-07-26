package com.jiuaoedu.framework.handler.core;


import com.jiuaoedu.framework.handler.api.IHandler;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/25 17:48
 */
public class Handler<T> implements IHandler<T> {

    @Override
    public boolean canHandle(T t) {
        return true;
    }

    @Override
    public void handle(T t) {
        // do nothing
    }
}
