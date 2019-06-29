package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class CallServer {

    public static final int ERROR_LIMIT = 10;
    public static final int PORT = 9000;
    public ArrayList<CallClientHandler> Clients;
    private boolean _running = false;
    private ServerSocket _serverSocket;
    private Thread _thread;

    private Runnable _runnable = new Runnable() {
        @Override
        public void run() {
            while (_running) {
                System.out.println("Waiting for client...");
                try {
                    Socket sock = _serverSocket.accept();
                    System.out.println("\tav.Client accepted > " + sock.getLocalPort());

                    CallClientHandler cl = new CallClientHandler(sock);
                    Clients.add(cl);
                } catch (Exception ex) {
                    System.out.println("\tError in accepting the client!!!");
                }
            }
        }
    };


    public CallServer() throws IOException {
        System.out.println("Creating Socket...");
        _serverSocket = new ServerSocket(PORT);
        Clients = new ArrayList<CallClientHandler>();
    }

    public void Start() {
        Stop();
        _running = true;
        _thread = new Thread(_runnable);
        _thread.start();
    }

    public void Stop() {
        if (_thread != null) {
            try {
                _running = false;
                _thread.interrupt();
            } catch (Exception ex) {}
            _thread = null;
        }
    }

    public void SendToAll(byte[] buffer, int offset, int count) {
        //sending the data to each connected client
        for (int i = 0; i < Clients.size(); i++) {
            CallClientHandler cl = Clients.get(i);
            try {
                cl.Send(buffer, offset, count);

                //resets error count of that client
                cl.ErrorCount = 0;
            } catch (Exception ex) {
                cl.ErrorCount++;
                System.out.println(cl.RemoteAddress.toString() + " @ Send Error#" + cl.ErrorCount);
                if (cl.ErrorCount >= ERROR_LIMIT) {
                    //Error limit reached
                    System.out.println("\tError limit exceeded.");
                    System.out.println("\tRemoving client " + cl.RemoteAddress.toString());

                    //removing the client from the list
                    Clients.remove(i);
                    i -= 1;
                }
            }
        }
    }
}
