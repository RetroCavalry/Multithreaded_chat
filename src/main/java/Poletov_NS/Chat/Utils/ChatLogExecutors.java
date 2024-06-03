package Poletov_NS.Chat.Utils;

import Poletov_NS.Chat.Handler.ClientHandlerExecutors;
import Poletov_NS.Chat.ServerListener.ServerListenerExecutors;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatLogExecutors {
    private final List<String> chatHistory = new ArrayList<>();
    private final ExecutorService executorService = Executors.newCachedThreadPool(); // Использование пула потоков

    public synchronized void put(String message, ClientHandlerExecutors clientSender) {
        chatHistory.add(message);
        System.out.println(message);
        broadcastMessage(message, clientSender);
    }

    private void broadcastMessage(String message, ClientHandlerExecutors clientSender) {
        for (ClientHandlerExecutors client : ServerListenerExecutors.getClients()) {
            if (client != clientSender) {
                executorService.submit(() -> {
                    try {
                        client.sendMessageToClient(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }
}
