package com.jiuaoedu.framework.communication.core.communicator.subcribe;

import com.jiuaoedu.framework.communication.api.communicator.Communicable;
import com.jiuaoedu.framework.communication.api.communicator.type.subcribe.IEventBus;
import com.jiuaoedu.framework.communication.api.communicator.type.subcribe.TopicSubscribable;
import com.jiuaoedu.framework.communication.api.message.IMessage;
import com.jiuaoedu.framework.communication.core.pojo.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/25 15:32
 */
public class PubSubCommunicationComponent implements Communicable, TopicSubscribable {
    private final String componentId;
    private final IEventBus eventBus;
    private final Set<String> subscribedTopics = ConcurrentHashMap.newKeySet();
    private static final Logger logger = LoggerFactory.getLogger(PubSubCommunicationComponent.class);

    public PubSubCommunicationComponent(String componentId, IEventBus eventBus) {
        this.componentId = componentId;
        this.eventBus = eventBus;
    }

    @Override
    public void subscribe(String topic) {
        subscribedTopics.add(topic);
        eventBus.subscribe(topic, this);
        logger.info("已订阅主题: {}", topic);
    }

    @Override
    public void unsubscribe(String topic) {
        subscribedTopics.remove(topic);
        eventBus.unsubscribe(topic, this);
        logger.info("已取消订阅主题: {}", topic);
    }

    @Override
    public Set<String> getSubscribedTopics() {
        return Collections.unmodifiableSet(subscribedTopics);
    }

    @Override
    public void sendMessage(IMessage message) {
        // 在发布订阅模式中，receiverId 被视为主题
        String topic = message.getReceiverId();
        logger.info("[发布消息] {} -> 主题:{} [{}]: {}",
                getComponentId(),
                topic,
                message.getType(),
                message.getContent());

        eventBus.publish(topic, message);
    }

    @Override
    public void receiveMessage(IMessage message) {
        // 只处理订阅的主题
        if (subscribedTopics.contains(message.getReceiverId())) {
            logger.info("[订阅接收] {} <- 主题:{} [{}]: {}",
                    getComponentId(),
                    message.getReceiverId(),
                    message.getType(),
                    message.getContent());

            processMessage(message);
        }
    }

    @Override
    public String getComponentId() {
        return componentId;
    }

    protected void processMessage(IMessage message) {
        // 由子类实现具体消息处理逻辑
    }
}
