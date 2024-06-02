package Poletov_NS.Chat.Server;

import Poletov_NS.Chat.utils.ChatLog;
import Poletov_NS.Chat.handlers.ClientHandler;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

// Класс сервера, который принимает подключения от клиентов и обрабатывает их
public class ChatServerExecutors {
    private static final int PORT = 27015;
    private static final ExecutorService executor = Executors.newFixedThreadPool(10);
    private static final List<ClientHandler> clients = Collections.synchronizedList(new ArrayList<>());
    private static final Logger logger = Logger.getLogger(ChatServer.class.getName());

    static {
        // Настройка логгера
        logger.setLevel(Level.ALL);
        try {
            // Перезапись файла лога при каждом запуске сервера
            FileHandler fileHandler = new FileHandler("src/logs/ChatServerExecutors.log", false);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Не удалось создать файл лога", e);
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);
        logger.info("Сервер запущен и слушает порт " + PORT);

        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(clientSocket, new ChatLog());
                clients.add(clientHandler);
                executor.execute(clientHandler); // Запускаем задачу в пуле потоков
            }
        } finally {
            serverSocket.close();
            executor.shutdown();
        }
    }

    // Метод для рассылки сообщений всем клиентам, кроме отправителя
    public static void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }

    // Метод для удаления клиента из списка активных подключений
    public static void removeClient(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        System.out.println("Клиент отключен: " + clientHandler.getClientSocket());
    }

    public static void logMessage(String message) {
        logger.info(message);
    }
}