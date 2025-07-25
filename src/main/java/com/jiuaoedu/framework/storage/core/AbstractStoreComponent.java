package com.jiuaoedu.framework.storage.core;

import com.jiuaoedu.framework.storage.api.EvictionPolicy;
import com.jiuaoedu.framework.storage.api.Storable;
import com.jiuaoedu.framework.storage.core.policy.LRUEvictionPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/25 11:54
 */
public abstract class AbstractStoreComponent<K, V> implements Storable<K, V>{

    private final int capacity;
    private final Map<K, V> storage;
    private final EvictionPolicy<K, V> evictionPolicy;
    private static final Logger logger = LoggerFactory.getLogger(AbstractStoreComponent.class);

    /**
     * 构造存储功能的组件
     * @param capacity 最大存储消息数量
     * @param evictionPolicy 淘汰策略
     */
    protected AbstractStoreComponent(int capacity, EvictionPolicy<K, V> evictionPolicy) {
        super();
        this.capacity = capacity;
        this.evictionPolicy = evictionPolicy;
        this.storage = evictionPolicy.createStorage(capacity);
    }

    /**
     * 使用默认的 LRU 淘汰策略
     * @param capacity 最大存储消息数量
     */
    protected AbstractStoreComponent(int capacity) {
        this(capacity, new LRUEvictionPolicy<>());
    }

    /**
     * 使用默认容量和 LRU 淘汰策略
     */
    protected AbstractStoreComponent() {
        this(10, new LRUEvictionPolicy<>());
    }

    @Override
    public void storeValue(V value) {
        // 假设V是带ID的对象（如Message），需子类实现键提取逻辑
        K key = extractKey(value);
        synchronized (storage) {
            if (evictionPolicy.shouldEvict(storage, capacity)) {
                K eldestKey = evictionPolicy.getEldestKey(storage);
                storage.remove(eldestKey);
                logger.info("存储容量已满，丢弃最老消息: {}", eldestKey);
            }
            storage.put(key, value);
            logger.info("存储条目：{}", key);
        }
    }

    @Override
    public V getValue(K key) {
        synchronized (storage) {
            return storage.get(key);
        }
    }

    /**
     * 获取存储容器中的所有实体
     *
     * @return 包含所有实体的Map（键为K，值为V）
     */
    @Override
    public Map<K, V> getAllValues() {
        synchronized (storage) {
            return new LinkedHashMap<>(storage);
        }
    }

    /**
     * 清空存储容器中的所有实体
     */
    @Override
    public void clearStorage() {
        synchronized (storage) {
            storage.clear();
            logger.info("已清空所有存储的消息");
        }
    }

    /**
     * 获取当前存储的实体数量
     *
     * @return 实体数量
     */
    @Override
    public int getStorageSize() {
        synchronized (storage) {
            return storage.size();
        }
    }

    /**
     * 获取存储容器的最大容量
     *
     * @return 最大可存储的实体数量
     */
    @Override
    public int getStorageCapacity() {
        return capacity;
    }

    protected abstract K extractKey(V value);
}
