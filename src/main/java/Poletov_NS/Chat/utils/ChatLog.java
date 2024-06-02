package Poletov_NS.Chat.utils;

import Poletov_NS.Chat.handlers.ClientHandler;
import Poletov_NS.Chat.Server.ChatServer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Класс для управления историей чата
public class ChatLog {
    private final List<String> chatHistory = Collections.synchronizedList(new ArrayList<>());

    public synchronized void put(String message, ClientHandler clientSender) {
        chatHistory.add(message);
        ChatServer.broadcastMessage(message, clientSender);
        // Используем геттер для получения имени пользователя из clientSender
        ChatServer.logMessage(clientSender.getNickName() + ": " + message);
    }
}
