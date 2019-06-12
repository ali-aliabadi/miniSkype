package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

public class Server {

    private ServerSocket serverSocket;
    private ArrayList<ClientHandler> clientHandlers;

    Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    void startServer() throws IOException {
        while (true) {
            clientHandlers.add(new ClientHandler(serverSocket.accept()));
        }
    }

}
