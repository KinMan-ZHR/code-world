package com.jiuaoedu.communication.framework.api.communicator.type.p2p;

import com.jiuaoedu.communication.framework.api.communicator.Communicable;

public interface DirectConnectable {
    void connect(Communicable peer);
    void disconnect(Communicable peer);
    boolean isConnected(Communicable peer);
}