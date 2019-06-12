package server;

import org.bson.Document;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable {

    Socket client;
    DataOutputStream print;
    DataInputStream scan;

    public ClientHandler(Socket client) {
        this.client=client;
        Thread newClient=new Thread(this);
        newClient.start();
    }

    @Override
    public void run() {
        String command;
        while (true) {
            try {
                command = scan.readUTF();
                Document doc = Document.parse(command);
                System.out.println(doc);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}