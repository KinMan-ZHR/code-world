package com.jiuaoedu.framework.communication.api.communicator.type.mediator;

public interface MediatorRegistrable {
    /**
     * 组件主动注册到中介者
     * @param mediator 中介者实例
     */
    void registerToMediator(Mediator mediator);
}