package jiuaoedu.communicationframework.core.base;

import com.jiuaoedu.communicationframework.api.communicator.Communicable;
import com.jiuaoedu.communicationframework.api.mediator.Mediator;
import com.jiuaoedu.communicationframework.api.message.Message;
import com.jiuaoedu.communicationframework.api.message.MessageType;
import com.jiuaoedu.communicationframework.core.base.AbstractCommunicationComponent;
import org.junit.Test;

public class AbstractCommunicationComponentTest {

    @Test
    public void test() {
        Mediator mediator = new SystemMediator();

        Communicable client = new AbstractCommunicationComponent("client") {
            @Override
            protected void processMessage(Message message) {
                System.out.println("客户端收到响应: " + message.getContent());
            }
        };

        Communicable userService = new UserService("userService");

        client.setMediator(mediator);
        userService.setMediator(mediator);

        // 发送查询请求
        Message request = new Message(
                "client",
                "userService",
                "查询用户张三",
                MessageType.REQUEST
        );

        client.sendMessage(request);
    }
}
