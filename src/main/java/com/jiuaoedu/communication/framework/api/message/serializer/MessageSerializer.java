package com.jiuaoedu.communication.framework.api.message.serializer;

import com.jiuaoedu.communication.framework.api.message.Message;

public interface MessageSerializer {
    String serialize(Message message);
    Message deserialize(String serializedMessage);
}
