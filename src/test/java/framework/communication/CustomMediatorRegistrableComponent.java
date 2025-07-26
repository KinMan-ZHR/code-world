package framework.communication;

import com.jiuaoedu.framework.communication.api.message.IMessage;
import com.jiuaoedu.framework.communication.api.message.MessageType;
import com.jiuaoedu.framework.communication.core.communicator.mediator.MediatorRegistrableCommunicationComponent;
import com.jiuaoedu.framework.communication.core.pojo.Message;
import com.jiuaoedu.framework.communication.core.pojo.MessageBuilder;

import javax.annotation.PostConstruct;

// 自定义中介者注册组件，用于处理消息
public class CustomMediatorRegistrableComponent extends MediatorRegistrableCommunicationComponent {
    public CustomMediatorRegistrableComponent(String componentId) {
        super(componentId);
    }

    @Override
    protected void processMessage(IMessage message) {
        System.out.println(this.getComponentId() +"处理来自"+ message.getSenderId() + "的消息: " + message.getContent());
        if (message.getType() == MessageType.SYN) {
//            sendMessage(message);//无限循环的bug,需要想办法解决
            if ("SYN".equals(message.getOperationType())) {
                Message synAckMessage = new MessageBuilder()
                        .from(this.getComponentId())
                        .to(message.getSenderId())
                        .ofType(MessageType.SYN)
                        .withContent("SYN+ACK 消息")
                        .signOperationType("SYN+ACK")
                        .build();
                sendMessage(synAckMessage);
            }
            else if ("SYN+ACK".equals(message.getOperationType())) {
                Message ackMessage = new MessageBuilder()
                        .from(this.getComponentId())
                        .to(message.getSenderId())
                        .ofType(MessageType.SYN)
                        .withContent("ACK 响应消息")
                        .signOperationType("ACK")
                        .build();
                sendMessage(ackMessage);
            }
        }
    }
}
