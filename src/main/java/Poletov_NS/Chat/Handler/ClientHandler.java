package Poletov_NS.Chat.Handler;

import Poletov_NS.Chat.Utils.ChatLog;
import Poletov_NS.Chat.ServerListener.ServerListener;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class ClientHandler implements Runnable {
    private Socket clientSocket;
    private ChatLog chatLog;
    private BufferedReader in;
    private BufferedWriter out;

    public ClientHandler(Socket clientSocket, ChatLog chatLog) {
        this.clientSocket = clientSocket;
        this.chatLog = chatLog;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            String nickName = in.readLine();
            chatLog.put(nickName + " connected to chat", this);

            while (!Thread.currentThread().isInterrupted()) {
                String message = in.readLine();
                if (Objects.isNull(message)) {
                    break;
                }
                System.out.println(nickName + ": " + message);
                chatLog.put(nickName + ": " + message, this);
            }
            chatLog.put(nickName + " disconnected from chat", this);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ServerListener.removeClient(this);
            closeResources();
        }
    }

    public void sendMessageToClient(String msg) throws IOException {
        if (!clientSocket.isOutputShutdown()) {
            out.write(msg);
            out.newLine();
            out.flush();
        }
    }

    private void closeResources() {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
        } catch (IOException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}

