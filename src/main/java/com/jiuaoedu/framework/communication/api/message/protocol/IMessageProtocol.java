package com.jiuaoedu.framework.communication.api.message.protocol;

/**
 * 消息协议接口
 * 定义消息签名、验证、提取等核心功能
 * 
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/25 12:00
 */
public interface IMessageProtocol {
    
    /**
     * 根据协议构造签名
     *
     * @return 构造的签名字符串
     */
    String constructSignature();

    /**
     * 从内容中提取签名
     * 
     * @param content 包含签名的内容
     * @return 提取的签名，如果不存在则返回null
     */
    String extractSignature(String content);
    
    /**
     * 从签名中提取值
     * 
     * @param signature 签名字符串
     * @return 提取的值，如果无法提取则返回null
     */
    String extractValueFromSignature(String signature);

    /**
     * 从内容中提取值
     *
     * @param content 包含签名的内容
     * @return 提取的值，如果无法提取则返回null
     */
    String extractValue(String content);
    
    /**
     * 验证签名是否存在
     * 
     * @param content 要验证的内容
     * @return true表示存在有效签名，false表示不存在或签名无效
     */
    boolean hasSignature(String content);
    
    /**
     * 从内容中移除签名
     * 
     * @param content 包含签名的内容
     * @return 移除签名后的内容
     */
    String returnSignatureRemovedContent(String content);
    
    /**
     * 验证签名格式是否正确
     * 
     * @param signature 要验证的签名
     * @return true表示格式正确，false表示格式错误
     */
    boolean verifySignatureFormat(String signature);
    
    /**
     * 将签名添加到内容中
     * 
     * @param content 原始内容
     * @return 添加签名后的内容
     */
    String returnSignatureAddedContent(String content);
    
    /**
     * 获取协议名称
     * 
     * @return 协议名称
     */
    String getProtocolName();
    
    /**
     * 获取协议版本
     * 
     * @return 协议版本
     */
    String getProtocolVersion();

    /**
     * 获取协议键
     *
     * @return 协议键
     */
    String getProtocolKey();
}
