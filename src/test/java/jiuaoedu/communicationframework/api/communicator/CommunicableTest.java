package jiuaoedu.communicationframework.api.communicator;

import com.jiuaoedu.communicationframework.api.message.Message;
import com.jiuaoedu.communicationframework.api.message.MessageBuilder;
import com.jiuaoedu.communicationframework.api.message.MessageType;
import com.jiuaoedu.communicationframework.core.base.AbstractCommunicationComponent;
import com.jiuaoedu.communicationframework.core.mediator.SystemMediator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class CommunicableTest {
    private SystemMediator mediator;
    private TestComponent sender;
    private TestComponent receiver;

    @BeforeEach
    void setUp() {
        mediator = new SystemMediator();
        sender = new TestComponent("sender");
        receiver = new TestComponent("receiver");
        
        sender.setMediator(mediator);
        receiver.setMediator(mediator);
    }

    @Test
    void testSendAndReceiveMessage() {
        Message message = new MessageBuilder()
            .from(sender.getComponentId())
            .to(receiver.getComponentId())
            .withContent("Hello World!")
            .ofType(MessageType.REQUEST)
            .build();
            
        sender.sendMessage(message);
        
        // 验证接收者收到消息
        assertTrue(receiver.receivedMessages.contains(message.getContent()));
    }

    @Test
    void testNonExistentReceiver() {
        Message message = new MessageBuilder()
            .from(sender.getComponentId())
            .to("non-existent")
            .withContent("Test")
            .ofType(MessageType.NOTIFICATION)
            .build();
            
        sender.sendMessage(message);
        
        // 验证发送者收到错误通知
        assertTrue(sender.receivedMessages.stream()
            .anyMatch(msg -> msg.contains("接收者不存在")));
    }

    private static class TestComponent extends AbstractCommunicationComponent {
        public final java.util.List<String> receivedMessages = new java.util.ArrayList<>();

        public TestComponent(String componentId) {
            super(componentId);
        }

        @Override
        protected void processMessage(Message message) {
            receivedMessages.add(message.getContent());
        }
    }
}
