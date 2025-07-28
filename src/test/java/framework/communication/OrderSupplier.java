package framework.communication;

import com.jiuaoedu.framework.communication.api.message.IMessage;
import com.jiuaoedu.framework.communication.api.message.MessageType;
import com.jiuaoedu.framework.communication.core.communicator.mediator.MediatorRegistrableCommunicationComponent;
import com.jiuaoedu.framework.communication.core.pojo.Message;
import com.jiuaoedu.framework.communication.core.pojo.MessageBuilder;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/28 15:28
 */
public class OrderSupplier extends MediatorRegistrableCommunicationComponent{
    protected OrderSupplier(String componentId) {
        super(componentId);
    }

    @Override
    protected void processMessage(IMessage message) {
        if (message.getType() == MessageType.REQUEST){
            System.out.println("Order组件处理来自"+message.getSenderId()+"的请求："+message.getContent());
            Message response = new MessageBuilder()
                    .from(this.getComponentId())
                    .to(message.getSenderId())
                    .withContent("{}")
                    .ofType(MessageType.RESPONSE)
                    .build();
            sendMessage(response);
        }
    }
}
