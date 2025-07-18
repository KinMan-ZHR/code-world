package com.jiuaoedu.communicationframework.extension.interceptor;

import com.jiuaoedu.communicationframework.api.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingInterceptor implements MessageInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);

    @Override
    public boolean preHandle(Message message) {
        logger.info("拦截到消息[PRE]: {}", message);
        return true; // 不拦截消息
    }

    @Override
    public void postHandle(Message message) {
        logger.info("消息处理完成[POST]: {}", message);
    }
}
