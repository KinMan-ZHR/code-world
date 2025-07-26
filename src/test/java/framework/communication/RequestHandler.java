package framework.communication;

import com.jiuaoedu.framework.communication.api.communicator.Communicable;
import com.jiuaoedu.framework.communication.api.message.IMessage;
import com.jiuaoedu.framework.communication.api.message.handler.IMessageHandler;
import com.jiuaoedu.framework.communication.core.pojo.Message;
import com.jiuaoedu.framework.communication.core.pojo.MessageBuilder;
import com.jiuaoedu.framework.communication.api.message.MessageType;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/25 18:53
 */
public class RequestHandler implements IMessageHandler {

    private final Communicable component;

    public RequestHandler(Communicable component) {
        this.component = component;
    }
    @Override
    public boolean canHandle(IMessage t) {
        return t.getType() == MessageType.REQUEST;
    }

    @Override
    public void handle(IMessage message) {
        Message message1 = new MessageBuilder()
                .fromMessage(message)
                .ofType(MessageType.RESPONSE)
                .build();
        component.receiveMessage(message1);
    }
}
