package jiuaoedu.communicationframework.extension.serializer;

import com.jiuaoedu.communicationframework.api.message.Message;
import com.jiuaoedu.communicationframework.api.message.MessageBuilder;
import com.jiuaoedu.communicationframework.api.message.MessageType;
import com.jiuaoedu.communicationframework.extension.serializer.XmlSerializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class XmlSerializerTest {
    private XmlSerializer serializer = new XmlSerializer();

    @Test
    void testSerializeAndDeserialize() {
        Message original = new MessageBuilder()
            .from("sender")
            .to("receiver")
            .withContent("Hello XML")
            .ofType(MessageType.NOTIFICATION)
            .build();
            
        String xml = serializer.serialize(original);
        Message deserialized = serializer.deserialize(xml);
        
        assertEquals(original.getSenderId(), deserialized.getSenderId());
        assertEquals(original.getReceiverId(), deserialized.getReceiverId());
        assertEquals(original.getContent(), deserialized.getContent());
        assertEquals(original.getType(), deserialized.getType());
    }
}
