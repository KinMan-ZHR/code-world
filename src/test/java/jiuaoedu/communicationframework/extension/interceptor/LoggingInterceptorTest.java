package jiuaoedu.communicationframework.extension.interceptor;

import com.jiuaoedu.communicationframework.api.message.Message;
import com.jiuaoedu.communicationframework.api.message.MessageBuilder;
import com.jiuaoedu.communicationframework.api.message.MessageType;
import com.jiuaoedu.communicationframework.extension.interceptor.LoggingInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LoggingInterceptorTest {
    private LoggingInterceptor interceptor;
    private Message message;

    @BeforeEach
    void setUp() {
        interceptor = new LoggingInterceptor();
        message = new MessageBuilder()
            .from("testSender")
            .to("testReceiver")
            .withContent("Test Message")
            .ofType(MessageType.REQUEST)
            .build();
    }

    @Test
    void testPreHandle() {
        interceptor.preHandle(message);
    }

    @Test
    void testPostHandle() {
        interceptor.postHandle(message);
    }
}
