package com.jiuaoedu.common.api;

import java.util.Map;

/**
 * 通用存储接口 - 定义实体的基本存储操作
 * @param <K> 键（Key）类型，用于唯一标识存储的实体
 * @param <V> 值（Value）类型，即存储的实体类型
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/25 11:18
 */
public interface Storable<K, V> {
    /**
     * 将实体存入存储容器
     * @param value 要存储的实体
     */
    void storeValue(V value);           // 增加"Value"标识，明确操作对象

    /**
     * 根据键从存储容器中获取实体
     * @param key 实体的唯一标识
     * @return 对应的实体，不存在则返回null
     */
    V getValue(K key);                  // 增加"Value"标识

    /**
     * 获取存储容器中的所有实体
     * @return 包含所有实体的Map（键为K，值为V）
     */
    Map<K, V> getAllValues();           // 增加"Values"标识

    /**
     * 清空存储容器中的所有实体
     */
    void clearStorage();                // 增加"Storage"标识，明确操作目标

    /**
     * 获取当前存储的实体数量
     * @return 实体数量
     */
    int getStorageSize();               // 增加"Storage"标识

    /**
     * 获取存储容器的最大容量
     * @return 最大可存储的实体数量
     */
    int getStorageCapacity();           // 增加"Storage"标识
}
