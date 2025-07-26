package com.jiuaoedu.framework.communication.api.message.protocol;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/26 17:32
 */
public interface IProtocol {
    String sign(String context, String signature);
    boolean verify(String signature);
    String extractSignature(String context);
}
