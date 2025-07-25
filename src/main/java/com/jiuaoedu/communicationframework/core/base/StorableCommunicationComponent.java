package com.jiuaoedu.communicationframework.core.base;

import com.jiuaoedu.communicationframework.api.communicator.Communicable;
import com.jiuaoedu.communicationframework.api.communicator.MessageStorable;
import com.jiuaoedu.communicationframework.api.communicator.StorableCommunicable;
import com.jiuaoedu.communicationframework.api.message.Message;

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
    public void storeValue(Message value) {
        messageStorage.storeValue(value);
    }

    @Override
    public Message getValue(String key) {
        return messageStorage.getValue(key);
    }

    @Override
    public Map<String, Message> getAllValues() {
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
    public void sendMessage(Message message) {
        communicator.sendMessage(message);
    }

    @Override
    public void doReceiveMessage(Message message) {
        communicator.receiveMessage(message);
    }

    @Override
    public String getComponentId() {
        return communicator.getComponentId();
    }
}
