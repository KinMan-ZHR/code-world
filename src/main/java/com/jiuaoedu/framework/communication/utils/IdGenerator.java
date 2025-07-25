package com.jiuaoedu.framework.communication.utils;

import java.util.UUID;

public class IdGenerator {

    private IdGenerator() {
        throw new IllegalStateException("Utility class");
    }
    public static String generateUniqueId() {
        return UUID.randomUUID().toString();
    }

    public static String generateUniqueId(String prefix) {
        return prefix + "-" + generateUniqueId();
    }
}