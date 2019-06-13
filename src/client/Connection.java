package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Connection {

    private static Socket socket;
    public static DataInputStream scan;
    public static DataOutputStream print;

    public Connection(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        scan = new DataInputStream(socket.getInputStream());
        print = new DataOutputStream(socket.getOutputStream());
    }

}
