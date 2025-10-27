package csc435.app;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Dispatcher implements Runnable {
    private ServerSideEngine engine;
    private int port;
    private ServerSocket serverSocket;
    private AtomicBoolean running = new AtomicBoolean(true);

    public Dispatcher(ServerSideEngine engine, int port) {
        this.engine = engine;
        this.port = port;
        try {
            this.serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (running.get()) {
            try {
                Socket clientSocket = serverSocket.accept();
                if (running.get()) { // Double-check after accept
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                    engine.spawnWorker(clientSocket);
                } else {
                    clientSocket.close();
                }
            } catch (IOException e) {
                if (running.get()) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void shutdown() {
        running.set(false);
        closeServerSocket();
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
