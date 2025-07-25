package com.jiuaoedu.framework.storage.api;

import java.util.Map;

/**
 * 通用存储淘汰策略接口 - 定义实体的基本存储淘汰操作
 * @param <K> 键（Key）类型，用于唯一标识存储的实体
 * @param <V> 值（Value）类型，即存储的实体类型
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/25 11:18
 */
public interface EvictionPolicy<K, V> {
    Map<K, V> createStorage(int capacity);
    boolean shouldEvict(Map<K, V> storage, int capacity);
    K getEldestKey(Map<K, V> storage);
}