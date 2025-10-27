package csc435.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Worker implements Runnable {
    private Socket clientSocket;
    private IndexStore store;
    private ServerSideEngine engine;
    private BufferedReader in;
    private PrintWriter out;

    public Worker(Socket clientSocket, IndexStore store, ServerSideEngine engine) {
        this.clientSocket = clientSocket;
        this.store = store;
        this.engine = engine;
        try {
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String command;
        try {
            while ((command = in.readLine()) != null) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
                if (command.startsWith("index")) {
                    handleIndexCommand();
                } else if (command.startsWith("search")) {
                    long startTime = System.nanoTime();  // Start time measurement
                    String query = command.substring(7).trim();
                    String results = engine.lookupIndex(query);
                    out.println(results);
                    out.println("search completed");
                    long endTime = System.nanoTime();
                    System.out.println("Search handling time: " + (endTime - startTime) / 1_000_000_000.0 + " seconds");
                } else if (command.equals("quit")) {
                    break;
                } else {
                    out.println("unrecognized command");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                engine.removeClient(clientSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleIndexCommand() throws IOException {
        long startTime = System.nanoTime();  // Start time measurement

        String line;
        while (!(line = in.readLine()).equals("index completed")) {
            String[] parts = line.split("\\|");
            if (parts.length == 3) {
                String filePath = parts[0];
                String word = parts[1];
                int frequency = Integer.parseInt(parts[2]);
                store.insertWord(filePath, word, frequency);
            }
        }
        out.println("index received");

        long endTime = System.nanoTime();  // End time measurement
        System.out.println("Index handling time: " + (endTime - startTime) / 1_000_000_000.0 + " seconds");
    }
}
