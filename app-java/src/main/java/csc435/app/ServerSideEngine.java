package csc435.app;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class ServerSideEngine {
    private IndexStore store;
    private ExecutorService threadPool;
    private Dispatcher dispatcher;
    private List<Socket> connectedClients;

    public ServerSideEngine(IndexStore store) {
        this.store = store;
        threadPool = Executors.newFixedThreadPool(16); // Max 16 threads
        connectedClients = new CopyOnWriteArrayList<>();
    }

    public void initialize(int port) {
        dispatcher = new Dispatcher(this, port);
        new Thread(dispatcher).start();
    }

    public void spawnWorker(Socket clientSocket) {
        connectedClients.add(clientSocket);
        threadPool.execute(new Worker(clientSocket, store, this));
    }

    public void shutdown() {
        if (dispatcher != null) {
            dispatcher.shutdown();
        }
        threadPool.shutdownNow();
        for (Socket clientSocket : connectedClients) {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        connectedClients.clear();
        System.out.println("Server shut down.");
    }

    public void list() {
        System.out.println("Connected clients:");
        for (Socket clientSocket : connectedClients) {
            System.out.println(clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort());
        }
    }

    public void removeClient(Socket clientSocket) {
        connectedClients.remove(clientSocket);
    }

    // New method to handle search from the main hashtable
    public synchronized String lookupIndex(String query) {
        return store.lookupIndex(query);
    }
}
