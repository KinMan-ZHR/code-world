package framework.communication;

import com.jiuaoedu.framework.communication.api.communicator.type.mediator.IMediator;
import com.jiuaoedu.framework.communication.core.pojo.Message;
import com.jiuaoedu.framework.communication.core.pojo.MessageBuilder;
import com.jiuaoedu.framework.communication.api.message.MessageType;
import com.jiuaoedu.framework.communication.core.communicator.mediator.mediators.SystemMediator;
import com.jiuaoedu.framework.communication.extension.strategy.MessageStrategyChain;
import com.jiuaoedu.framework.communication.extension.strategy.RequestResponseStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MediatorTest {
    CustomMediatorRegistrableComponent componentA;
    CustomMediatorRegistrableComponent componentB;
    IMediator mediator;
    @BeforeEach
    void setUp() {
        // 创建两个通信组件
        componentA = new CustomMediatorRegistrableComponent("ComponentA");
        componentB = new CustomMediatorRegistrableComponent("ComponentB");
        RequestResponseStrategy requestResponseStrategy = new RequestResponseStrategy(new MessageBuilder());
        requestResponseStrategy.registerRequestHandler("SYN", new RequestContextHandler());
        mediator = new SystemMediator(new MessageStrategyChain(requestResponseStrategy));
        componentA.registerToMediator(mediator);
        componentB.registerToMediator(mediator);
    }

    @Test
    void testSync() {
        // 第一次握手：ComponentA 发送 SYN 到 ComponentB
        Message synMessage = new MessageBuilder()
                .from("ComponentA")
                .to("ComponentB")
                .ofType(MessageType.SYN)
                .withContent("SYN 消息")
                .signOperationType("SYN")
                .build();
        componentA.sendMessage(synMessage);
    }

    @Test
    void testRequest() {
        // 发送请求：ComponentA 发送 GET_USER_INFO 到 ComponentB
        Message requestMessage = new MessageBuilder()
                .from("ComponentA")
                .to("ComponentB")
                .ofType(MessageType.REQUEST)
                .withContent("GET_USER_INFO")
                .signOperationType("GET_USER_INFO")
                .build();
                componentA.sendMessage(requestMessage);
    }
}