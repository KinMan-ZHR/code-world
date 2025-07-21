package jiuaoedu.communicationframework.core.base;

import com.jiuaoedu.communicationframework.api.message.Message;
import com.jiuaoedu.communicationframework.api.message.MessageBuilder;
import com.jiuaoedu.communicationframework.api.message.MessageType;
import com.jiuaoedu.communicationframework.core.base.AbstractCommunicationComponent;
import com.jiuaoedu.communicationframework.core.mediator.SystemMediator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbstractCommunicationComponentTest {
    private SystemMediator mediator;
    private TestComponent component;

    @BeforeEach
    void setUp() {
        mediator = new SystemMediator();
        component = new TestComponent("testComponent");
        component.setMediator(mediator);
    }

    @Test
    void testSendMessage() {
        Message message = new MessageBuilder()
            .from(component.getComponentId())
            .to("receiver")
            .withContent("Test Send")
            .ofType(MessageType.EVENT)
            .build();
            
        component.sendMessage(message);
        
        // 验证中介者收到消息
        assertEquals(1, mediator.getMessageStats().get("EVENT"));
    }

    @Test
    void testReceiveMessage() {
        Message message = new MessageBuilder()
            .from("sender")
            .to(component.getComponentId())
            .withContent("Test Receive")
            .ofType(MessageType.NOTIFICATION)
            .build();
            
        // 直接通过中介者发送消息
        mediator.dispatchMessage(message);
        
        // 验证组件收到消息
        assertTrue(component.receivedMessages.contains("Test Receive"));
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

