package com.jiuaoedu.framework.communication.api.communicator.type.p2p;

import com.jiuaoedu.framework.communication.api.communicator.Communicable;

public interface DirectConnectable {
    void connect(Communicable peer);
    void disconnect(Communicable peer);
    boolean isConnected(Communicable peer);
}