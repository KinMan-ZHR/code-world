package jiuaoedu.communicationframework.core.exception;

import com.jiuaoedu.communicationframework.api.message.Message;
import com.jiuaoedu.communicationframework.api.message.MessageBuilder;
import com.jiuaoedu.communicationframework.api.message.MessageType;
import com.jiuaoedu.communicationframework.extension.interceptor.LoggingInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class LoggingInterceptorTest {
    private LoggingInterceptor interceptor;
    private Logger mockLogger;
    private Message message;

    @BeforeEach
    void setUp() {
        interceptor = new LoggingInterceptor();
        
        // 使用Mockito替换真实的logger
        mockLogger = mock(Logger.class);
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");
        LoggerFactory.getLogger(LoggingInterceptor.class).equals(mockLogger);
        
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
        
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockLogger, times(1)).info(captor.capture());
        
        assertTrue(captor.getValue().contains("拦截到消息[PRE]"));
        assertTrue(captor.getValue().contains("Test Message"));
    }

    @Test
    void testPostHandle() {
        interceptor.postHandle(message);
        
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(mockLogger, times(1)).info(captor.capture());
        
        assertTrue(captor.getValue().contains("消息处理完成[POST]"));
        assertTrue(captor.getValue().contains("Test Message"));
    }
}
