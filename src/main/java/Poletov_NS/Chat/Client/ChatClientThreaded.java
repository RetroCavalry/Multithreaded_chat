package Poletov_NS.Chat.Client;

import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatClientThreaded {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private final String serverAddress;
    private final int serverPort;
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public ChatClientThreaded(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
    }

    public void start() throws IOException {
        // Подключение к серверу
        socket = new Socket(serverAddress, serverPort);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        // Поток для чтения сообщений с сервера
        executor.execute(() -> {
            try {
                String receivedMessage;
                while ((receivedMessage = in.readLine()) != null) {
                    System.out.println(receivedMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        executor.execute(() -> {
            try {
                Scanner scanner = new Scanner(System.in);
                while (!socket.isClosed()) {
                    String messageToSend = scanner.nextLine();
                    out.println(messageToSend);
                }
                scanner.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }


    public static void main(String[] args) {
        ChatClient client = new ChatClient("127.0.0.1", 27015);
        try {
            client.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}