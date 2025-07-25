package com.jiuaoedu.communication.framework.core.communication_component.p2p;

import com.jiuaoedu.communication.framework.api.communicator.Communicable;
import com.jiuaoedu.communication.framework.api.communicator.type.p2p.DirectConnectable;
import com.jiuaoedu.communication.framework.api.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/25 15:22
 */
public class DirectCommunicationComponent implements Communicable, DirectConnectable {
    private final String componentId;
    private final Map<String, Communicable> connectedPeers = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(DirectCommunicationComponent.class);

    public DirectCommunicationComponent(String componentId) {
        this.componentId = componentId;
    }

    @Override
    public synchronized void connect(Communicable peer) {
        connectedPeers.put(peer.getComponentId(), peer);
        logger.info("已连接到 peer: {}", peer.getComponentId());

        // 双向连接（可选，根据实际需求决定是否需要）
        if (peer instanceof DirectConnectable) {
            ((DirectConnectable) peer).connect(this);
        }
    }

    @Override
    public synchronized void disconnect(Communicable peer) {
        connectedPeers.remove(peer.getComponentId());
        logger.info("已断开与 peer 的连接: {}", peer.getComponentId());

        // 双向断开（如果之前建立了双向连接）
        if (peer instanceof DirectConnectable) {
            ((DirectConnectable) peer).disconnect(this);
        }
    }

    @Override
    public boolean isConnected(Communicable peer) {
        return connectedPeers.containsKey(peer.getComponentId());
    }

    @Override
    public void sendMessage(Message message) {
        Communicable receiver = connectedPeers.get(message.getReceiverId());
        if (receiver == null) {
            logger.error("错误: 未连接到目标 peer - {}", message.getReceiverId());
            return;
        }

        logger.info("[点对点发送] {} -> {} [{}]: {}",
                getComponentId(),
                message.getReceiverId(),
                message.getType(),
                message.getContent());

        receiver.receiveMessage(message);
    }

    @Override
    public void receiveMessage(Message message) {
        logger.info("[点对点接收] {} <- {} [{}]: {}",
                getComponentId(),
                message.getSenderId(),
                message.getType(),
                message.getContent());

        processMessage(message);
    }

    @Override
    public String getComponentId() {
        return componentId;
    }

    protected void processMessage(Message message) {
        // 由子类实现具体消息处理逻辑
    }
}