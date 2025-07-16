package com.jiuaoedu.communicationframework.utils;

import java.util.UUID;

public class IdGenerator {
    public static String generateUniqueId() {
        return UUID.randomUUID().toString();
    }

    public static String generateUniqueId(String prefix) {
        return prefix + "-" + generateUniqueId();
    }
}