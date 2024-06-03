package Poletov_NS.Chat.ServerListener;

import Poletov_NS.Chat.Handler.ClientHandlerExecutors;
import Poletov_NS.Chat.Utils.ChatLogExecutors;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerListenerExecutors {
    private ServerSocket serverSocket;
    private static final List<ClientHandlerExecutors> clients = new ArrayList<>();
    private final ChatLogExecutors chatLog = new ChatLogExecutors();
    private ExecutorService executorService = Executors.newFixedThreadPool(5); // Пул потоков

    public void start() throws IOException {
        serverSocket = new ServerSocket(27015);
        try {
            while (!serverSocket.isClosed()) {
                try {
                    Socket incomingConnection = serverSocket.accept();
                    ClientHandlerExecutors client = new ClientHandlerExecutors(incomingConnection, chatLog);
                    synchronized (clients) {
                        clients.add(client);
                    }
                    executorService.submit(client);
                } catch (IOException e) {
                    if (!serverSocket.isClosed()) {
                        e.printStackTrace();
                        // Решите, нужно ли перезапустить сервер или остановить его.
                    }
                }
            }
        } finally {
            stopServer();
        }
    }

    public ServerListenerExecutors(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void stopServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        // Ожидание завершения всех задач или принудительное завершение после таймаута
    }

    public static synchronized List<ClientHandlerExecutors> getClients() {
        return clients;
    }

    public static synchronized void removeClient(ClientHandlerExecutors clientHandler) {
        clients.remove(clientHandler);
        System.out.println("Client " + clientHandler + " has been removed.");
    }
}