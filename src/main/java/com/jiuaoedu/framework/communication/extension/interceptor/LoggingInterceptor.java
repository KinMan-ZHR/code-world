package com.jiuaoedu.framework.communication.extension.interceptor;

import com.jiuaoedu.framework.communication.api.interceptor.IMessageInterceptor;
import com.jiuaoedu.framework.communication.api.message.IMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingInterceptor implements IMessageInterceptor {
    private final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(IMessage message) {
        logger.info("拦截到消息[PRE]: {}", message);
        return true; // 不拦截消息
    }

    @Override
    public void postHandle(IMessage message) {
        logger.info("消息处理完成[POST]: {}", message);
    }
}
