package Poletov_NS.Chat.ServerListener;

import Poletov_NS.Chat.Handler.ClientHandler;
import Poletov_NS.Chat.Utils.ChatLog;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerListener {
    private ServerSocket serverSocket;
    private static final List<ClientHandler> clients = new ArrayList<>();
    private final ChatLog chatLog = new ChatLog();

    public void start() throws IOException {
        serverSocket = new ServerSocket(27015);
        while (true) {
            Socket incomingConnection = serverSocket.accept();
            ClientHandler client = new ClientHandler(incomingConnection, chatLog);
            clients.add(client);
            new Thread(client).start();
        }
    }

    public static synchronized List<ClientHandler> getClients() {
        return clients;
    }

    public static synchronized void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        System.out.println("Client " + clientHandler + " has been removed.");
    }
}

