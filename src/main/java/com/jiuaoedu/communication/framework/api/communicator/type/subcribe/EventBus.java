package com.jiuaoedu.communication.framework.api.communicator.type.subcribe;

import com.jiuaoedu.communication.framework.api.message.Message;

// 事件总线接口
public interface EventBus {
    void publish(String topic, Message message);
    void subscribe(String topic, TopicSubscribable subscriber);
    void unsubscribe(String topic, TopicSubscribable subscriber);
}
