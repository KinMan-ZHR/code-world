package com.jiuaoedu.communicationframework.extension.serializer;

import com.jiuaoedu.communicationframework.api.message.Message;

public interface MessageSerializer {
    String serialize(Message message);
    Message deserialize(String serializedMessage);
}
