package com.jiuaoedu.framework.storage.core.policy;


import com.jiuaoedu.framework.storage.api.EvictionPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashMap;
import java.util.Map;

// LRU 淘汰策略实现
public class LRUEvictionPolicy<K, V> implements EvictionPolicy<K, V> {
    private final Logger logger = LoggerFactory.getLogger(LRUEvictionPolicy.class);
    @Override
    public Map<K, V> createStorage(int capacity) {
        return new LinkedHashMap<K, V>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                boolean shouldRemove = size() > capacity;
                if (shouldRemove) {
                    logger.info("存储容量已满，丢弃最老消息: {}", eldest.getKey());
                }
                return shouldRemove;
            }
        };
    }

    @Override
    public boolean shouldEvict(Map<K, V> storage, int capacity) {
        return storage.size() > capacity;
    }

    @Override
    public K getEldestKey(Map<K, V> storage) {
        return storage.keySet().iterator().next();
    }
}