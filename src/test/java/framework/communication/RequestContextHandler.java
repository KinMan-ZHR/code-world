package framework.communication;

import com.jiuaoedu.framework.communication.api.message.context.handler.IMessageContextHandler;
import com.jiuaoedu.framework.communication.api.message.context.IMessageContext;
import com.jiuaoedu.framework.communication.api.message.MessageType;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/25 18:53
 */
public class RequestContextHandler implements IMessageContextHandler {

    @Override
    public boolean canHandle(IMessageContext messageContext) {
        return messageContext.getOriginalMessage().getType() == MessageType.REQUEST;
    }

    @Override
    public void handle(IMessageContext messageContext) {
        System.out.println("RequestContextHandler: " + messageContext.getOriginalMessage());
    }
}
