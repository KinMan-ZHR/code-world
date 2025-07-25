package com.jiuaoedu.framework.communication.api.communicator.type.subcribe;

import com.jiuaoedu.framework.communication.api.message.Message;

// 事件总线接口
public interface EventBus {
    void publish(String topic, Message message);
    void subscribe(String topic, TopicSubscribable subscriber);
    void unsubscribe(String topic, TopicSubscribable subscriber);
}
