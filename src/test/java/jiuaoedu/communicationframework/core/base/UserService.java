package jiuaoedu.communicationframework.core.base;

import com.jiuaoedu.communicationframework.api.message.Message;
import com.jiuaoedu.communicationframework.api.message.MessageType;
import com.jiuaoedu.communicationframework.core.base.AbstractCommunicationComponent;

public class UserService extends AbstractCommunicationComponent {
    public UserService(String componentId) {
        super(componentId);
    }

    @Override
    protected void processMessage(Message message) {
        switch (message.getType()) {
            case REQUEST:
                handleRequest(message);
                break;
            case COMMAND:
                handleCommand(message);
                break;
            default:
                System.out.println("未处理的消息类型: " + message.getType());
        }
    }

    private void handleRequest(Message message) {
        String content = message.getContent();
        if (content.startsWith("查询用户")) {
            String userId = content.substring(4);
            String responseContent = "用户" + userId + "信息：[姓名: " + userId + ", 状态: 活跃]";
            
            Message response = new Message(
                getComponentId(),
                message.getSenderId(),
                responseContent,
                MessageType.RESPONSE
            );
            
            sendMessage(response);
        }
    }

    private void handleCommand(Message message) {
        System.out.println("执行命令: " + message.getContent());
        // 执行命令逻辑
    }
}
