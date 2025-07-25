package com.jiuaoedu.framework.communication.core.communication_component.subcribe.eventbus;

import com.jiuaoedu.framework.communication.api.communicator.Communicable;
import com.jiuaoedu.framework.communication.api.communicator.type.subcribe.EventBus;
import com.jiuaoedu.framework.communication.api.communicator.type.subcribe.TopicSubscribable;
import com.jiuaoedu.framework.communication.api.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryEventBus implements EventBus {
    private final Map<String, Set<TopicSubscribable>> topicSubscribers = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(InMemoryEventBus.class);

    @Override
    public void publish(String topic, Message message) {
        logger.info("发布消息到主题: {}", topic);
        Set<TopicSubscribable> subscribers = topicSubscribers.getOrDefault(topic, Collections.emptySet());
        
        // 同步发布（可改为异步实现）
        subscribers.forEach(subscriber -> {
            try {
                if (subscriber instanceof Communicable) {
                    ((Communicable) subscriber).receiveMessage(message);
                }
            } catch (Exception e) {
                logger.error("消息发布到订阅者失败: {}", subscriber.getComponentId(), e);
            }
        });
    }

    @Override
    public synchronized void subscribe(String topic, TopicSubscribable subscriber) {
        topicSubscribers.computeIfAbsent(topic, k -> ConcurrentHashMap.newKeySet())
                        .add(subscriber);
        logger.info("订阅者 {} 已订阅主题: {}", subscriber.getComponentId(), topic);
    }

    @Override
    public synchronized void unsubscribe(String topic, TopicSubscribable subscriber) {
        Set<TopicSubscribable> subscribers = topicSubscribers.get(topic);
        if (subscribers != null) {
            subscribers.remove(subscriber);
            logger.info("订阅者 {} 已取消订阅主题: {}", subscriber.getComponentId(), topic);
        }
    }
}