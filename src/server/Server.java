package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

class Server {

    private ServerSocket serverSocket, voiceServerSocket;
    static final ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    static final ArrayList<VoiceClientHandler> vch = new ArrayList<>();
    static Integer chatId = 1;

    Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        voiceServerSocket = new ServerSocket(12012);
    }

    void startServer() throws IOException {
        Thread thread = new Thread(this::acceptVoice);
        thread.start();
        while (true) {
            clientHandlers.add(new ClientHandler(serverSocket.accept()));
        }
    }

    private void acceptVoice() {
        while (true) {
            try {
                vch.add(new VoiceClientHandler(voiceServerSocket.accept(), voiceServerSocket.accept()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
