package com.jiuaoedu.framework.communication.api.communicator.type.subcribe;

import java.util.Set;

// 主题订阅能力接口
public interface TopicSubscribable {
    void subscribe(String topic);
    void unsubscribe(String topic);
    Set<String> getSubscribedTopics();
    String getComponentId();
}

