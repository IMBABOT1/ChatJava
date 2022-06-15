package ru.geekbrains.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainApp {

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(8189)){
              Socket socket = serverSocket.accept();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
