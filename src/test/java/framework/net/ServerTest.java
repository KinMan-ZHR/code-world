package framework.net;

import com.jiuaoedu.framework.net.Server;
import com.jiuaoedu.framework.net.common.MessageUtils;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServerTest {

    @Test
    void stServerStartsAndBindsToPort() throws IOException {
        int port = 12345;
        try (Server server = new Server(port, 1000);
             ServerSocket testSocket = new ServerSocket()) {
            
            // 验证端口已被占用
            assertThrows(BindException.class,
                () -> new Server(port));
        }
    }

    @Test
    public void testMessageProcessing() throws Exception {
        // 模拟Socket和MessageUtils
        Socket mockSocket = mock(Socket.class);
        when(MessageUtils.getMessage(mockSocket)).thenReturn("test");

        // 执行处理逻辑
        Server server = new Server(12345, 1000);
        server.process(mockSocket);

        // 验证消息处理和发送
        MessageUtils.sendMessage(mockSocket, "processedtest");
        verify(mockSocket).close();
    }

    @Test
    public void testServerStop() throws Exception {
        Server server = new Server(12345, 1000);
        Thread serverThread = new Thread(server);
        serverThread.start();

        // 等待服务器启动
        Thread.sleep(500);

        // 停止服务器
        server.stopProcessing();
        serverThread.join(1000);

        // 验证服务器已停止
        assertFalse(serverThread.isAlive());
    }


}