package com.jiuaoedu.framework.communication.core.communicator;

import com.jiuaoedu.framework.communication.api.communicator.Communicable;
import com.jiuaoedu.framework.communication.api.communicator.extension.MessageStorable;
import com.jiuaoedu.framework.communication.api.communicator.extension.StorableCommunicable;
import com.jiuaoedu.framework.communication.api.message.IMessage;

import java.util.Map;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/25 09:58
 */
public class StorableCommunicationComponent implements StorableCommunicable {

    private final MessageStorable messageStorage;
    private final Communicable communicator;

    public StorableCommunicationComponent(MessageStorable messageStorage, Communicable communicator) {
        this.messageStorage = messageStorage;
        this.communicator = communicator;
    }

    @Override
    public void storeValue(IMessage value) {
        messageStorage.storeValue(value);
    }

    @Override
    public IMessage getValue(String key) {
        return messageStorage.getValue(key);
    }

    @Override
    public Map<String, IMessage> getAllValues() {
        return messageStorage.getAllValues();
    }

    @Override
    public void clearStorage() {
       messageStorage.clearStorage();
    }

    @Override
    public int getStorageSize() {
        return messageStorage.getStorageSize();
    }

    @Override
    public int getStorageCapacity() {
        return messageStorage.getStorageCapacity();
    }

    @Override
    public void sendMessage(IMessage message) {
        communicator.sendMessage(message);
    }

    @Override
    public void doReceiveMessage(IMessage message) {
        communicator.receiveMessage(message);
    }

    @Override
    public String getComponentId() {
        return communicator.getComponentId();
    }
}
