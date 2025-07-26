package com.jiuaoedu.framework.communication.extension.strategy;

import com.jiuaoedu.framework.communication.api.message.context.IMessageContext;
import com.jiuaoedu.framework.communication.api.message.context.handler.strategy.IMessageStrategyChain;
import com.jiuaoedu.framework.handler.api.IHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/26 11:29
 */
public class MessageStrategyChain implements IMessageStrategyChain {
    private final List<IHandler<IMessageContext>> strategies = new ArrayList<>();

    public MessageStrategyChain(List<IHandler<IMessageContext>> strategies) {
        this.strategies.addAll(strategies);
    }

    public MessageStrategyChain(IHandler<IMessageContext> strategy) {
        this.strategies.add(strategy);
    }

    public MessageStrategyChain() {
    }

    @Override
    public void addNextHandler(IHandler<IMessageContext> handler) {
        strategies.add(handler);
    }

    @Override
    public void process(IMessageContext messageContext) {
        for (IHandler<IMessageContext> strategy : strategies) {
            if (strategy.canHandle(messageContext)) {
                strategy.handle(messageContext);

                // 如果消息已被处理且无需继续处理，则终止链
                if (messageContext.isHandled() || !messageContext.shouldForward()) {
                    break;
                }
            }
        }
    }
}
