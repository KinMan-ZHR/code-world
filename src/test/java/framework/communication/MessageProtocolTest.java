package framework.communication;

import com.jiuaoedu.framework.communication.api.message.protocol.IMessageProtocol;
import com.jiuaoedu.framework.communication.core.protocol.CorrelationMessageProtocol;

/**
 * 消息协议测试类
 * 演示重构后的消息协议接口的使用
 */
public class MessageProtocolTest {
    
    public static void main(String[] args) {
        System.out.println("=== 消息协议接口重构测试 ===\n");
        
        // 测试关联ID协议
        testCorrelationProtocol();
        
        System.out.println("\n" + "==================================================" + "\n");

    }
    
    private static void testCorrelationProtocol() {
        System.out.println("1. 测试关联ID协议 (CorrelationMessageProtocol)");
        
        IMessageProtocol protocol = new CorrelationMessageProtocol();
        System.out.println("   协议名称: " + protocol.getProtocolName());
        System.out.println("   协议版本: " + protocol.getProtocolVersion());
        
        // 测试构造签名
        String signature = protocol.constructSignature();
        System.out.println("   构造签名: " + signature);
        
        // 测试添加签名到内容
        String originalContent = "Hello World";
        String signedContent = protocol.returnSignatureAddedContent(originalContent);
        System.out.println("   原始内容: " + originalContent);
        System.out.println("   签名后内容: " + signedContent);
        
        // 测试验证签名存在
        boolean hasSig = protocol.hasSignature(signedContent);
        System.out.println("   是否包含签名: " + hasSig);
        
        // 测试提取签名
        String extractedSignature = protocol.extractSignature(signedContent);
        System.out.println("   提取的签名: " + extractedSignature);
        
        // 测试提取键和值
        String extractedKey = protocol.getProtocolKey();
        String extractedValue = protocol.extractValueFromSignature(extractedSignature);
        System.out.println("   提取的键: " + extractedKey);
        System.out.println("   提取的值: " + extractedValue);
        
        // 测试移除签名
        String removedContent = protocol.returnSignatureRemovedContent(signedContent);
        System.out.println("   移除签名后内容: " + removedContent);
        
        // 测试签名格式验证
        boolean isValidFormat = protocol.verifySignatureFormat(extractedSignature);
        System.out.println("   签名格式是否有效: " + isValidFormat);
    }
} 