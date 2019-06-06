package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

import static java.lang.System.exit;

public class Server implements ServerStart {

    ServerSocket serverSocket;
    Set<ClientHandler> clientHandlers = new HashSet<>();
    Set<User> users = new HashSet<>();

    Server(int port) {

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("error creating server");
            exit(0);
        }

    }

    @Override
    public void start() {

        while (true) {

            try {
                clientHandlers.add(new ClientHandler(serverSocket.accept()));
                System.out.println("a client connected successfully");
            } catch (IOException e) {
                System.err.println("error in accepting client.");
            }

        }

    }
}
