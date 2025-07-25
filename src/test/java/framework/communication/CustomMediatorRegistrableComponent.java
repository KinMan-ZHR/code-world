package framework.communication;

import com.jiuaoedu.framework.communication.api.message.Message;
import com.jiuaoedu.framework.communication.core.communication_component.mediator.MediatorRegistrableCommunicationComponent;

// 自定义中介者注册组件，用于处理消息
public class CustomMediatorRegistrableComponent extends MediatorRegistrableCommunicationComponent {
    public CustomMediatorRegistrableComponent(String componentId) {
        super(componentId);
    }

    @Override
    protected void processMessage(Message message) {
        System.out.println("处理消息: " + message.getContent());
    }
}
