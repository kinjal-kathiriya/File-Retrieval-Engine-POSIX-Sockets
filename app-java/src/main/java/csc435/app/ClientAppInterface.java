package csc435.app;

import java.lang.System;
import java.util.Scanner;

public class ClientAppInterface {
    private ClientSideEngine engine;

    public ClientAppInterface(ClientSideEngine engine) {
        this.engine = engine;
    }

    public void readCommands() {
        Scanner sc = new Scanner(System.in);
        String command;
        
        while (true) {
            System.out.print("> ");
            command = sc.nextLine();

            if (command.equals("quit")) {
                engine.closeConnection();
                break;
            } 

            else if (command.startsWith("connect")) {
                String[] parts = command.split(" ");
                if (parts.length == 3) {
                    String ipAddress = parts[1];
                    int port = Integer.parseInt(parts[2]);
                    try {
                        engine.openConnection(ipAddress, port);
                        System.out.println("Connection successful!");
                    } catch (Exception e) {
                        System.out.println("Failed to connect: " + e.getMessage());
                    }
                } else {
                    System.out.println("Invalid connect command format.");
                }
            } 
            
            // indexing
            else if (command.startsWith("index")) {
               String directoryPath = command.substring(6);
               try {
                    engine.indexFiles(directoryPath);
                    System.out.println("Indexing completed!");
                } catch (Exception e) {
                    System.out.println("Failed to index files: " + e.getMessage());
                }
            }
            
            // search
            else if (command.startsWith("search")) {
                String query = command.substring(7);
               try {
                    String results = engine.searchFiles(query);
                    System.out.println("Search results: " + results);
                } catch (Exception e) {
                    System.out.println("Failed to search files: " + e.getMessage());
                }
            }

            else {
                System.out.println("Unrecognized command!");
            }
        }

        sc.close();
    }
}
