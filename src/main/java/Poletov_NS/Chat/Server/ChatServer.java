package Poletov_NS.Chat.Server;

import Poletov_NS.Chat.ServerListener.ServerListener;

import java.io.IOException;

public class ChatServer {
    public static void main(String[] args) throws IOException {
        ServerListener serverListener = new ServerListener();
        serverListener.start();
    }
}

