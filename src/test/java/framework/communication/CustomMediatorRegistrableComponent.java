package framework.communication;

import com.jiuaoedu.framework.communication.api.message.IMessage;
import com.jiuaoedu.framework.communication.core.pojo.Message;
import com.jiuaoedu.framework.communication.core.communicator.mediator.MediatorRegistrableCommunicationComponent;

// 自定义中介者注册组件，用于处理消息
public class CustomMediatorRegistrableComponent extends MediatorRegistrableCommunicationComponent {
    public CustomMediatorRegistrableComponent(String componentId) {
        super(componentId);
    }

    @Override
    protected void processMessage(IMessage message) {
        System.out.println(this.getComponentId() +"处理来自"+ message.getSenderId() + "的消息: " + message.getContent());
    }
}
