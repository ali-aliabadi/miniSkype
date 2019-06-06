package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements ClientHandlerCommunication {

    Socket socket;
    DataInputStream dataInputStream;
    DataOutputStream dataOutputStream;
    boolean isLoggedIn = false;


    ClientHandler(Socket socket) {

        this.socket = socket;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("error in getting streams");
        }

    }


    @Override
    public void notificationListener() {

    }

    @Override
    public void commandHandler() {

    }

    @Override
    public void login() {

    }

    @Override
    public void signup() {

    }
}
