package framework.net;

import com.jiuaoedu.framework.net.Server;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.net.Socket;
import static org.junit.jupiter.api.Assertions.*;

public class ServerIntegrationTest {

    @Test
    public void testEndToEndCommunication() throws Exception {
        int port = 12345;
        try (Server server = new Server(port, 5000)) {
            Thread serverThread = new Thread(server);
            serverThread.start();
            
            // 创建客户端连接
            try (Socket clientSocket = new Socket("localhost", port);
                 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
                
                // 发送消息
                out.println("hello");
                
                // 接收响应
                String response = in.readLine();
                assertEquals("processedhello", response);
            }
            
            server.stopProcessing();
            serverThread.join();
        }
    }

    @Test
    public void testConcurrentClients() throws Exception {
        int port = 12345;
        int clientCount = 5;

        try (Server server = new Server(port, 5000)) {
            Thread serverThread = new Thread(server);
            serverThread.start();

            // 创建多个客户端线程
            Thread[] clientThreads = new Thread[clientCount];
            for (int i = 0; i < clientCount; i++) {
                final int clientId = i;
                clientThreads[i] = new Thread(() -> {
                    try (Socket clientSocket = new Socket("localhost", port);
                         PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                         BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                        out.println("client" + clientId);
                        String response = in.readLine();
                        assertEquals("processedclient" + clientId, response);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                clientThreads[i].start();
            }

            // 等待所有客户端完成
            for (Thread client : clientThreads) {
                client.join();
            }

            server.stopProcessing();
            serverThread.join();
        }
    }
}