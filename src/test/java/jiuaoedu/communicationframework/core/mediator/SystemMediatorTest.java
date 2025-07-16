package jiuaoedu.communicationframework.core.mediator;

import com.jiuaoedu.communicationframework.api.communicator.Communicable;
import com.jiuaoedu.communicationframework.api.message.Message;
import com.jiuaoedu.communicationframework.api.message.MessageBuilder;
import com.jiuaoedu.communicationframework.api.message.MessageType;
import com.jiuaoedu.communicationframework.core.base.AbstractCommunicationComponent;
import com.jiuaoedu.communicationframework.core.base.SystemMediator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SystemMediatorTest {
    private SystemMediator mediator;
    private Communicable componentA;
    private Communicable componentB;

    @BeforeEach
    void setUp() {
        mediator = new SystemMediator();
        componentA = new TestComponent("A");
        componentB = new TestComponent("B");
        
        mediator.registerComponent(componentA);
        mediator.registerComponent(componentB);
    }

    @Test
    void testRegisterAndUnregisterComponent() {
        mediator.unregisterComponent("A");
        assertFalse(mediator.getMessageStats().containsKey("A"));
    }

    @Test
    void testMessageStatistics() {
        Message request = new MessageBuilder()
            .from("A")
            .to("B")
            .withContent("Request")
            .ofType(MessageType.REQUEST)
            .build();
            
        Message response = new MessageBuilder()
            .from("B")
            .to("A")
            .withContent("Response")
            .ofType(MessageType.RESPONSE)
            .build();
            
        mediator.dispatchMessage(request);
        mediator.dispatchMessage(response);
        
        assertEquals(1, mediator.getMessageStats().get("REQUEST"));
        assertEquals(1, mediator.getMessageStats().get("RESPONSE"));
    }

    private static class TestComponent extends AbstractCommunicationComponent {
        public TestComponent(String componentId) {
            super(componentId);
        }

        @Override
        protected void processMessage(Message message) {
            // 空实现
        }
    }
}