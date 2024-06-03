package Poletov_NS.Chat.Server;

import Poletov_NS.Chat.ServerListener.ServerListenerExecutors;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatServerExecutors {
    private static ExecutorService executorService; // Пул потоков

    public static void main(String[] args) throws IOException {
        executorService = Executors.newFixedThreadPool(5); // Инициализация пула с 5 потоками
        ServerListenerExecutors serverListener = new ServerListenerExecutors(executorService);
        serverListener.start();
    }
}