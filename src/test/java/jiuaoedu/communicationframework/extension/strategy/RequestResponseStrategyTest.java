package jiuaoedu.communicationframework.extension.strategy;

import com.jiuaoedu.communicationframework.api.communicator.MessageHandler;
import com.jiuaoedu.communicationframework.api.message.Message;
import com.jiuaoedu.communicationframework.api.message.MessageBuilder;
import com.jiuaoedu.communicationframework.api.message.MessageType;
import com.jiuaoedu.communicationframework.extension.strategy.RequestResponseStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

class RequestResponseStrategyTest {
    private RequestResponseStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new RequestResponseStrategy();
    }

    @Test
    void testRequestResponseFlow() throws ExecutionException, InterruptedException, TimeoutException {
        // 注册请求处理器
        strategy.registerRequestHandler("GET_DATA", message -> {
            Message response = new MessageBuilder()
                .from("service")
                .to(message.getSenderId())
                .withContent("DATA: [1, 2, 3]")
                .ofType(MessageType.RESPONSE)
                .build();

            // 模拟回复消息
            strategy.handleResponse(response);
        });
        
        // 发送请求
        Message request = new MessageBuilder()
            .from("client")
            .to("service")
            .withContent("GET_DATA: userInfo")
            .ofType(MessageType.REQUEST)
            .build();
            
        CompletableFuture<Message> future = strategy.sendRequest(request);
        
        // 验证响应
        Message response = future.get(1, TimeUnit.SECONDS);
        assertEquals("DATA: [1, 2, 3]", response.getContent());
    }

    @Test
    void testUnsupportedRequestType() {
        Message request = new MessageBuilder()
            .from("client")
            .to("service")
            .withContent("UNKNOWN: operation")
            .ofType(MessageType.REQUEST)
            .build();
            
        strategy.handleRequest(request);
        
        // 验证错误响应
        // 注意：此测试需要根据实际实现调整
    }
}