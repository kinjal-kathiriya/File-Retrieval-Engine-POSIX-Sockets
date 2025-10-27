    package csc435.app;

    import java.util.Scanner;

    public class ServerAppInterface {
        private ServerSideEngine engine;

        public ServerAppInterface(ServerSideEngine engine) {
            this.engine = engine;
        }

        public void readCommands() {
            Scanner sc = new Scanner(System.in);
            String command;
            
            while (true) {
                System.out.print("> ");
                command = sc.nextLine();

                if (command.equals("quit")) {
                    engine.shutdown();
                    break;
                } 

                if (command.startsWith("list")) {
                    engine.list();
                    continue;
                }

                System.out.println("unrecognized command!");
            }

            sc.close();
        }
    }
