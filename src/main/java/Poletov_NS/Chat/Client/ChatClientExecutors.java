package Poletov_NS.Chat.Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatClientExecutors {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private Scanner scanner;
    private ExecutorService executorService; // Пул потоков

    public ChatClientExecutors(String serverAddress, int serverPort) throws IOException {
        socket = new Socket(serverAddress, serverPort);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        scanner = new Scanner(System.in);
        executorService = Executors.newFixedThreadPool(2); // Пул для двух потоков: чтение и запись
    }

    public void start() {
        // Поток для чтения сообщений с сервера
        executorService.submit(() -> {
            try {
                while (!socket.isClosed()) {
                    String serverMessage = in.readLine();
                    if (serverMessage != null) {
                        System.out.println("Сервер: " + serverMessage);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Поток для отправки сообщений на сервер
        executorService.submit(() -> {
            try {
                while (!socket.isClosed()) {
                    String userMessage = scanner.nextLine();
                    if ("exit".equalsIgnoreCase(userMessage)) {
                        socket.close();
                    } else {
                        out.write(userMessage);
                        out.newLine();
                        out.flush();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static void main(String[] args) {
        try {
            ChatClientExecutors client = new ChatClientExecutors("localhost", 27015);
            client.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
