package Poletov_NS.Chat.Client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private Socket socket;
    private BufferedReader in;
    private BufferedWriter out;
    private Scanner scanner;

    public ChatClient(String serverAddress, int serverPort) throws IOException {
        socket = new Socket(serverAddress, serverPort);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        scanner = new Scanner(System.in);
    }

    public void start() {
        // Поток для чтения сообщений с сервера
        Thread readThread = new Thread(() -> {
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
        Thread writeThread = new Thread(() -> {
            try {
                while (!socket.isClosed()) {
                    String userMessage = scanner.nextLine();
                    if ("exit".equalsIgnoreCase(userMessage)) {
                        socket.close();
                        break;
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

        readThread.start();
        writeThread.start();
    }

    public static void main(String[] args) {
        try {
            ChatClient client = new ChatClient("localhost", 27015);
            client.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



