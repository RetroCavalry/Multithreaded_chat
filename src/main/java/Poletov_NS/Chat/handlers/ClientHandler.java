package Poletov_NS.Chat.handlers;

import Poletov_NS.Chat.utils.ChatLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// Класс для обработки подключения каждого клиента
public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private ChatLog chatLog;
    private String nickName; // Переменная для хранения имени пользователя

    public ClientHandler(Socket socket, ChatLog chatLog) throws IOException {
        this.clientSocket = socket;
        this.chatLog = chatLog;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Запрос имени пользователя
        out.println("Введите ваше имя пользователя:");
        this.nickName = in.readLine(); // Считываем имя пользователя из потока ввода
    }

    @Override
    public void run() {
        try {
            // После получения имени пользователя, сообщаем о подключении в чат
            Poletov_NS.Chat.Server.ChatServerExecutors.logMessage(nickName + " connected to chat");

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                // Отправляем сообщение всем пользователям через ChatServerThreaded
                Poletov_NS.Chat.Server.ChatServerExecutors.broadcastMessage(nickName + ": " + inputLine, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Poletov_NS.Chat.Server.ChatServer.removeClient(this);
            try {
                in.close();
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public String getNickName() {
        return nickName;
    }


    public void sendMessage(String message) {
        out.println(message);
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
