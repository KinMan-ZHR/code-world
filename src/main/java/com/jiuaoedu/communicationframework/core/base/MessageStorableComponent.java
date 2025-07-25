package com.jiuaoedu.communicationframework.core.base;

import com.jiuaoedu.communicationframework.api.communicator.MessageStorable;
import com.jiuaoedu.communicationframework.api.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/25 09:58
 */
public abstract class MessageStorableComponent extends AbstractCommunicationComponent implements MessageStorable {

    private static final Logger logger = LoggerFactory.getLogger(MessageStorableComponent.class);
    private final int capacity;
    private final Map<String, Message> storedMessages;

    /**
     * 构造具有消息存储功能的组件
     * @param componentId 组件唯一标识
     * @param capacity 最大存储消息数量
     */
    protected MessageStorableComponent(String componentId, int capacity) {
        super(componentId);
        this.capacity = capacity;
        this.storedMessages = initMessageMap(capacity);
    }

    protected MessageStorableComponent(String componentId) {
        this(componentId, 10); // 默认存储10条消息
    }

    private Map<String, Message> initMessageMap(int maxSize) {
        return new LinkedHashMap<String, Message>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Message> eldest) {
                boolean shouldRemove = size() > maxSize;
                if (shouldRemove) {
                    logger.info("存储容量已满，丢弃最老消息: {}", eldest.getValue().getMessageId());
                }
                return shouldRemove;
            }
        };
    }

    /**
     * 存储消息到缓存中
     * 设为final防止子类篡改存储逻辑
     * @param message 要存储的消息
     */
    public final void storeMessage(Message message) {
        synchronized (storedMessages) {
            storedMessages.put(message.getMessageId(), message);
            logger.info("已存储消息: {}，内容: {}", message.getMessageId(), message.getContent());
        }
    }

    /**
     * 获取存储的消息
     * @param messageId 消息ID
     * @return 消息对象，如果不存在则返回null
     */
    public Message getStoredMessage(String messageId) {
        synchronized (storedMessages) {
            return storedMessages.get(messageId);
        }
    }

    /**
     * 获取当前存储的所有消息
     * @return 消息映射的副本
     */
    public Map<String, Message> getAllStoredMessages() {
        synchronized (storedMessages) {
            return new LinkedHashMap<>(storedMessages);
        }
    }

    /**
     * 清空存储的所有消息
     */
    public void clearStoredMessages() {
        synchronized (storedMessages) {
            storedMessages.clear();
            logger.info("已清空所有存储的消息");
        }
    }

    /**
     * 获取当前存储的消息数量
     * @return 消息数量
     */
    public int getStoredMessageCount() {
        synchronized (storedMessages) {
            return storedMessages.size();
        }
    }

    /**
     * 获取最大存储容量
     * @return 最大存储消息数量
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * 框架级消息处理流程（固定不变）
     * 1. 先存储原始消息（强制前置操作）
     * 2. 再调用子类的自定义处理逻辑
     */
    @Override
    protected final void processMessage(Message message) {
        // 第一步：固定存储原始消息（子类无法修改这一步）
        storeMessage(message);
        // 第二步：调用子类的自定义处理逻辑（扩展点）
        handleMessageAfterStorage(message);
    }

    /**
     * 子类必须实现的消息处理方法（存储后执行）
     * 这里定义为抽象方法，强制子类实现具体逻辑
     * @param message 已存储的原始消息
     */
    protected abstract void handleMessageAfterStorage(Message message);
}
