package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

class Server {

    private ServerSocket serverSocket;
    static final ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    static Integer chatId = 1;

    Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    void startServer() throws IOException {
        while (true) {
            clientHandlers.add(new ClientHandler(serverSocket.accept()));
        }
    }

}
