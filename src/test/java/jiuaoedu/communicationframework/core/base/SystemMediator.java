package jiuaoedu.communicationframework.core.base;

import com.jiuaoedu.communicationframework.api.communicator.Communicable;
import com.jiuaoedu.communicationframework.api.mediator.Mediator;
import com.jiuaoedu.communicationframework.api.message.Message;
import com.jiuaoedu.communicationframework.api.message.MessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SystemMediator implements Mediator {
    private final Map<String, Communicable> components = new ConcurrentHashMap<>();

    @Override
    public void registerComponent(Communicable component) {
        components.put(component.getComponentId(), component);
    }

    @Override
    public void unregisterComponent(String componentId) {
        components.remove(componentId);
    }

    @Override
    public void dispatchMessage(Message message) {
        Communicable receiver = components.get(message.getReceiverId());
        
        if (receiver == null) {
            System.err.println("错误: 接收者 " + message.getReceiverId() + " 不存在");
            // 发送失败通知
            Message failureNotice = new Message(
                "系统", 
                message.getSenderId(), 
                "消息发送失败：接收者不存在", 
                MessageType.NOTIFICATION
            );
            components.get(message.getSenderId()).receiveMessage(failureNotice);
            return;
        }
        
        // 可添加消息验证、日志、拦截器等
        receiver.receiveMessage(message);
    }
}