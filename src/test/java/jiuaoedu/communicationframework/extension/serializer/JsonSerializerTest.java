package jiuaoedu.communicationframework.extension.serializer;

import com.jiuaoedu.communicationframework.api.message.Message;
import com.jiuaoedu.communicationframework.api.message.MessageBuilder;
import com.jiuaoedu.communicationframework.api.message.MessageType;
import com.jiuaoedu.communicationframework.extension.serializer.JsonSerializer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/16 17:02
 */
class JsonSerializerTest {
    private JsonSerializer serializer = new JsonSerializer();

    @Test
    void testSerializeAndDeserialize() {
        Message original = new MessageBuilder()
                .from("sender")
                .to("receiver")
                .withContent("Hello JSON")
                .ofType(MessageType.COMMAND)
                .build();

        String json = serializer.serialize(original);
        Message deserialized = serializer.deserialize(json);

        assertEquals(original.getSenderId(), deserialized.getSenderId());
        assertEquals(original.getReceiverId(), deserialized.getReceiverId());
        assertEquals(original.getContent(), deserialized.getContent());
        assertEquals(original.getType(), deserialized.getType());
    }
}
