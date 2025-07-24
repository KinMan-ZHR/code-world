package jiuaoedu.communicationframework.extension.strategy;

import com.jiuaoedu.communicationframework.api.communicator.MessageHandler;
import com.jiuaoedu.communicationframework.api.message.Message;
import com.jiuaoedu.communicationframework.api.message.MessageBuilder;
import com.jiuaoedu.communicationframework.api.message.MessageType;
import com.jiuaoedu.communicationframework.api.protocol.DefaultMessageProtocol;
import com.jiuaoedu.communicationframework.api.protocol.MessageProtocol;
import com.jiuaoedu.communicationframework.extension.strategy.RequestResponseStrategy;
import com.jiuaoedu.communicationframework.utils.IdGenerator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RequestResponseStrategyTest {

    @Test
    void testHandleRequest_ShouldPreserveOperationType() {
        // 准备数据
        MessageProtocol protocol = new DefaultMessageProtocol();
        RequestResponseStrategy strategy = new RequestResponseStrategy();
        
        // 创建一个处理器并注册
        MessageHandler mockHandler = message -> {
            // 验证操作类型是否正确
            String operationType = protocol.extractOperationType(message.getContent());
            assertEquals("LOGIN", operationType);
        };
        strategy.registerRequestHandler("LOGIN", mockHandler);
        
        // 创建原始请求消息
        String content = protocol.generateContentWithOperationType("username=admin&password=123456", "LOGIN");
        Message request = new MessageBuilder(protocol)
                .from("client")
                .to("server")
                .withContent(content)
                .signOperationType("LOGIN")
                .ofType(MessageType.REQUEST)
                .build();
        
        // 处理请求
        strategy.handleRequest(request);
    }
}