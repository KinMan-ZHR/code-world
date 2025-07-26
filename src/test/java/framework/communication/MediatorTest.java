package framework.communication;

import com.jiuaoedu.framework.communication.api.communicator.type.mediator.IMediator;
import com.jiuaoedu.framework.communication.core.pojo.Message;
import com.jiuaoedu.framework.communication.core.pojo.MessageBuilder;
import com.jiuaoedu.framework.communication.api.message.MessageType;
import com.jiuaoedu.framework.communication.core.communicator.mediator.mediators.SystemMediator;
import com.jiuaoedu.framework.communication.extension.strategy.MessageStrategyChain;
import com.jiuaoedu.framework.communication.extension.strategy.RequestResponseStrategy;

public class MediatorTest {
    public static void main(String[] args) {

        // 创建两个通信组件
        CustomMediatorRegistrableComponent componentA = new CustomMediatorRegistrableComponent("ComponentA");
        CustomMediatorRegistrableComponent componentB = new CustomMediatorRegistrableComponent("ComponentB");
        RequestResponseStrategy requestResponseStrategy = new RequestResponseStrategy(new MessageBuilder());
        requestResponseStrategy.registerRequestHandler("SYN", new RequestHandler(componentB));
        IMediator mediator = new SystemMediator(new MessageStrategyChain(requestResponseStrategy));
        // 注册组件到中介者
        componentA.registerToMediator(mediator);
        componentB.registerToMediator(mediator);

        // 模拟三次握手
        // 第一次握手：ComponentA 发送 SYN 到 ComponentB
        Message synMessage = new MessageBuilder()
                .from("ComponentA")
                .to("ComponentB")
                .ofType(MessageType.REQUEST)
                .withContent("SYN 消息")
                .signOperationType("SYN")
                .build();
        componentA.sendMessage(synMessage);

        // 第二次握手：ComponentB 发送 SYN+ACK 到 ComponentA
        Message synAckMessage = new MessageBuilder()
                .from("ComponentB")
                .to("ComponentA")
                .ofType(MessageType.REQUEST)
                .withContent("SYN+ACK 消息")
                .signOperationType("SYN+ACK")
                .build();
        componentB.sendMessage(synAckMessage);

        // 第三次握手：ComponentA 发送 ACK 到 ComponentB
        Message ackMessage = new MessageBuilder()
                .from("ComponentA")
                .to("ComponentB")
                .ofType(MessageType.REQUEST)
                .withContent("ACK 消息")
                .signOperationType("ACK")
                .build();
        componentA.sendMessage(ackMessage);
    }
}