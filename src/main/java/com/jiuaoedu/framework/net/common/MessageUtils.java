package com.jiuaoedu.framework.net.common;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * @author ZhangHaoRan or KinMan Zhang
 * @since 2025/7/29 10:55
 */
public class MessageUtils {
    public static void sendMessage(Socket socket, String message) throws Exception{
        OutputStream stream = socket.getOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(stream);
        objectOutputStream.writeUTF(message);
        objectOutputStream.flush();
    }

    public static String getMessage(Socket socket) throws Exception{
        return new ObjectInputStream(socket.getInputStream()).readUTF();
    }
}
