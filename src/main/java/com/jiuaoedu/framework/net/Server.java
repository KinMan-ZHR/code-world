package com.jiuaoedu.framework.net;


import com.jiuaoedu.framework.net.common.MessageUtils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/29 09:55
 */
public class Server implements AutoCloseable, Runnable{
    ServerSocket serverSocket;
    volatile boolean keepProcessing = true;

    public Server(int port, int millisecondsTimeOut) throws IOException
    {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(millisecondsTimeOut);
    }

    public Server(int port) throws IOException {
        this(port, 5000);
    }

    @Override
    public void run() {
        System.out.println("Server started");
        while (keepProcessing) {
           try {
               System.out.println("Waiting for connection");
               Socket socket = serverSocket.accept();
               System.out.println("Connection accepted");
               process(socket);
           } catch (Exception e) {
               handle(e);
           }
        }
    }

    private void process(Socket socket){
        if (socket == null)
            return;
        Runnable clientHandler = () -> {
            try {
                System.out.println("Server: getting message");
                String message = MessageUtils.getMessage(socket);
                System.out.println("Server: message received: " + message);
                Thread.sleep(1000);
                System.out.println("Server: sending message" + message);
                MessageUtils.sendMessage(socket, "processed" + message);
                System.out.println("Server: message sent");
            } catch (Exception e) {
                handle(e);
            } finally {
                closeIgnoringException(socket);
            }
        };
        new Thread(clientHandler).start();

    }

    private void handle(Exception e){
        if(!(e instanceof SocketException)){
            e.printStackTrace();
        }
    }

    public void stopProcessing(){
        keepProcessing = false;
        closeIgnoringException(serverSocket);
    }

    private void closeIgnoringException(AutoCloseable closeable){
        if(closeable != null){
            try {
                closeable.close();
            } catch (Exception e) {
                handle(e);
            }
        }
    }

    @Override
    public void close() {
        stopProcessing();
    }
}