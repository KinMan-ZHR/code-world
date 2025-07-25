package com.jiuaoedu.framework.communication.api.communicator.type.mediator;

import com.jiuaoedu.framework.communication.api.interceptor.MessageInterceptor;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/23 14:27
 */
public interface ISecureMediator extends Mediator{
    void addInterceptor(MessageInterceptor interceptor);
}
