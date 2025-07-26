package com.jiuaoedu.framework.communication.api.message.serializer;

import com.jiuaoedu.framework.communication.api.message.IMessage;

public interface MessageSerializer {
    String serialize(IMessage message);
    IMessage deserialize(String serializedMessage);
}
