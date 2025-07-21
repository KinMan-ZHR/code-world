package jiuaoedu.communicationframework.core.mediator;

import com.jiuaoedu.communicationframework.api.message.Message;
import com.jiuaoedu.communicationframework.api.message.MessageBuilder;
import com.jiuaoedu.communicationframework.api.message.MessageType;
import com.jiuaoedu.communicationframework.core.base.AbstractCommunicationComponent;
import com.jiuaoedu.communicationframework.core.mediator.AsyncMediator;
import com.jiuaoedu.communicationframework.core.mediator.SystemMediator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AsyncMediatorTest {
    private AsyncMediator asyncMediator;
    private TestComponent sender;
    private TestComponent receiver;
    private CountDownLatch latch;

    @BeforeEach
    void setUp() {
        SystemMediator syncMediator = new SystemMediator();
        asyncMediator = new AsyncMediator(syncMediator);
        
        sender = new TestComponent("sender");
        receiver = new TestComponent("receiver") {
            @Override
            protected void processMessage(Message message) {
                super.processMessage(message);
                latch.countDown();
            }
        };
        
        sender.setMediator(asyncMediator);
        receiver.setMediator(asyncMediator);
        
        latch = new CountDownLatch(1);
    }

    @Test
    void testAsyncMessageDelivery() throws InterruptedException {
        Message message = new MessageBuilder()
            .from(sender.getComponentId())
            .to(receiver.getComponentId())
            .withContent("Async Test")
            .ofType(MessageType.EVENT)
            .build();
            
        sender.sendMessage(message);
        
        // 等待消息异步处理
        assertTrue(latch.await(2, TimeUnit.SECONDS));
        assertTrue(receiver.receivedMessages.contains("Async Test"));
        
        asyncMediator.shutdown();
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
