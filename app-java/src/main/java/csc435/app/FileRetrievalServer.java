package csc435.app;

public class FileRetrievalServer {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java FileRetrievalServer <port>");
            return;
        }

           int serverPort = Integer.parseInt(args[0]);

        // TO-DO initialize the number of worker threads from args[0]

        IndexStore store = new IndexStore();
        ServerSideEngine engine = new ServerSideEngine(store);
        ServerAppInterface appInterface = new ServerAppInterface(engine);
        
        // thread that creates & 
        // server TCP/IP socket 
        engine.initialize(serverPort);

        // read commands from the user
        appInterface.readCommands();
        
        
    }
}
