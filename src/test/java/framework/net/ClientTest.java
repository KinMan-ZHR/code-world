package framework.net;

import com.jiuaoedu.framework.net.Server;
import com.jiuaoedu.framework.net.common.MessageUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.net.Socket;

class ClientTest {
  private static final int PORT = 8009;
  private static final int TIMEOUT = 2000;

  Server server;
  Thread serverThread;

  @BeforeEach
  void createServer() throws Exception {
    try {
      server = new Server(PORT, TIMEOUT);
      serverThread = new Thread(server);
      serverThread.start();
    } catch (Exception e){
      e.printStackTrace(System.err);
      throw e;
    }
  }

  @AfterEach
  void shutdownServer() throws InterruptedException {
    if (server != null) {
      server.stopProcessing();
      serverThread.join();
    }
  }

  class TrivialClient implements Runnable {
    int clientNumber;

    TrivialClient(int clientNumber) {
      this.clientNumber = clientNumber;
    }

    public void run() {
      try {
        connectSendReceive(clientNumber);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Timeout(10000)
  @Test
  void shouldRunInUnder10Seconds() throws Exception {
    Thread[] threads = new Thread[10];

    for (int i = 0; i < threads.length; ++i) {
      threads[i] = new Thread(new TrivialClient(i));
      threads[i].start();
    }

    for (int i = 0; i < threads.length; ++i) {
      threads[i].join();
    }
  }

  private void connectSendReceive(int i) throws Exception {
    System.out.printf("Client %2d: connecting\n", i);
    Socket socket = new Socket("localhost", PORT);
    System.out.printf("Client %2d: sending message\n", i);
    MessageUtils.sendMessage(socket, Integer.toString(i));
    System.out.printf("Client %2d: getting reply\n", i);
    MessageUtils.getMessage(socket);
    System.out.printf("Client %2d: finished\n", i);
    socket.close();
  }
}
