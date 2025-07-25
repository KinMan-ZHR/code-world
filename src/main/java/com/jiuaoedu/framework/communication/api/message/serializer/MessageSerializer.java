package com.jiuaoedu.framework.communication.api.message.serializer;

import com.jiuaoedu.framework.communication.api.message.Message;

public interface MessageSerializer {
    String serialize(Message message);
    Message deserialize(String serializedMessage);
}
