package com.jiuaoedu.framework.communication.api.communicator.type.subcribe;

import com.jiuaoedu.framework.communication.api.message.IMessage;

// 事件总线接口
public interface IEventBus {
    void publish(String topic, IMessage message);
    void subscribe(String topic, TopicSubscribable subscriber);
    void unsubscribe(String topic, TopicSubscribable subscriber);
}
