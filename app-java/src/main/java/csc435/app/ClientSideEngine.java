package csc435.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class ClientSideEngine {
    private Socket socket;
    private PrintWriter out;
    private Scanner in;

    public ClientSideEngine() {
    }

    public void openConnection(String ipAddress, int port) throws IOException {
        socket = new Socket(ipAddress, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new Scanner(socket.getInputStream());
    }

    public void closeConnection() {
        try {
            if (socket != null && !socket.isClosed()) {
                out.println("quit");
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void indexFiles(String directoryPath) throws IOException {
        long startTime = System.nanoTime();  // Start time measurement
        File directory = new File(directoryPath);
        if (!directory.exists() || !directory.isDirectory()) {
            throw new IOException("Invalid directory path");
        }

        Map<String, Map<String, Integer>> fileWordFrequencyMap = new HashMap<>();

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isFile() && file.getName().endsWith(".txt")) {
                Map<String, Integer> wordFrequency = processFile(file);
                fileWordFrequencyMap.put(file.getAbsolutePath(), wordFrequency);
            }
        }

        out.println("index " + directoryPath);

        for (Map.Entry<String, Map<String, Integer>> entry : fileWordFrequencyMap.entrySet()) {
            String filePath = entry.getKey();
            Map<String, Integer> wordFrequency = entry.getValue();

            for (Map.Entry<String, Integer> wordEntry : wordFrequency.entrySet()) {
                String word = wordEntry.getKey();
                int frequency = wordEntry.getValue();
                out.println(filePath + "|" + word + "|" + frequency);
            }
        }

        out.println("index completed");

        String response = in.nextLine();
        if (!response.equals("index received")) {
            throw new IOException("Indexing failed on server");
        }
        long endTime = System.nanoTime();  // End time measurement
        System.out.println("Indexing time: " + (endTime - startTime) / 1_000_000_000.0 + " seconds");
    }

    private Map<String, Integer> processFile(File file) throws IOException {
        Map<String, Integer> wordFrequency = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split("\\W+");
                for (String word : words) {
                    if (!word.isEmpty()) {
                        wordFrequency.put(word, wordFrequency.getOrDefault(word, 0) + 1);
                    }
                }
            }
        }
        return wordFrequency;
    }

    public String searchFiles(String query) throws IOException {
        long startTime = System.nanoTime();  // Start time measurement

        out.println("search " + query);

        StringBuilder results = new StringBuilder();
        String response;
        while (!(response = in.nextLine()).equals("search completed")) {
            results.append(response).append("\n");
        }

        long endTime = System.nanoTime();  // End time measurement
        System.out.println("Search time: " + (endTime - startTime) / 1_000_000_000.0 + " seconds");

        return results.toString();
    }
}








    