package framework.communication;

import com.jiuaoedu.framework.communication.api.message.IMessage;
import com.jiuaoedu.framework.communication.api.message.MessageType;
import com.jiuaoedu.framework.communication.core.communicator.mediator.MediatorRegistrableCommunicationComponent;
import com.jiuaoedu.framework.communication.core.pojo.Message;
import com.jiuaoedu.framework.communication.core.pojo.MessageBuilder;

// 自定义中介者注册组件，用于处理消息
public class CustomMediatorRegistrableComponent extends MediatorRegistrableCommunicationComponent {
    public CustomMediatorRegistrableComponent(String componentId) {
        super(componentId);
    }

    @Override
    protected void processMessage(IMessage message) {
        System.out.println(this.getComponentId() +"处理来自"+ message.getSenderId() + "的消息: " + message.getContent());
        
        // 处理SYN类型的消息（三次握手）
        if (message.getType() == MessageType.SYN) {
            handleSynMessage(message);
        }
        // 处理REQUEST类型的消息
        else if (message.getType() == MessageType.REQUEST) {
            handleRequestMessage(message);
        }
        // 处理RESPONSE类型的消息
        else if (message.getType() == MessageType.RESPONSE) {
            handleResponseMessage(message);
        }
        // 处理ERROR类型的消息
        else if (message.getType() == MessageType.ERROR) {
            handleErrorMessage(message);
        }
    }
    
    private void handleSynMessage(IMessage message) {
        String operationType = message.getOperationType();
        
        if ("SYN".equals(operationType)) {
            // 收到SYN，发送SYN+ACK
            Message synAckMessage = new MessageBuilder()
                    .from(this.getComponentId())
                    .to(message.getSenderId())
                    .ofType(MessageType.SYN)
                    .withContent("SYN+ACK 消息")
                    .signOperationType("SYN+ACK")
                    .build();
            sendMessage(synAckMessage);
            System.out.println(this.getComponentId() + " 发送 SYN+ACK 到 " + message.getSenderId());
        }
        else if ("SYN+ACK".equals(operationType)) {
            // 收到SYN+ACK，发送ACK
            Message ackMessage = new MessageBuilder()
                    .from(this.getComponentId())
                    .to(message.getSenderId())
                    .ofType(MessageType.SYN)
                    .withContent("ACK 响应消息")
                    .signOperationType("ACK")
                    .build();
            sendMessage(ackMessage);
            System.out.println(this.getComponentId() + " 发送 ACK 到 " + message.getSenderId());
        }
        else if ("ACK".equals(operationType)) {
            // 收到ACK，连接建立完成
            System.out.println(this.getComponentId() + " 收到 ACK，连接建立完成");
        }
    }
    
    private void handleRequestMessage(IMessage message) {
        // 处理请求消息，发送响应
        Message responseMessage = new MessageBuilder()
                .from(this.getComponentId())
                .to(message.getSenderId())
                .ofType(MessageType.RESPONSE)
                .withContent("处理请求: " + message.getContent())
                .signOperationType("RESPONSE")
                .build();
        sendMessage(responseMessage);
        System.out.println(this.getComponentId() + " 处理请求并发送响应");
    }
    
    private void handleResponseMessage(IMessage message) {
        // 处理响应消息
        System.out.println(this.getComponentId() + " 收到响应: " + message.getContent());
    }
    
    private void handleErrorMessage(IMessage message) {
        // 处理错误消息
        System.out.println(this.getComponentId() + " 收到错误: " + message.getContent());
    }
}
