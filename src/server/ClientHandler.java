package server;

import constants.Communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements ClientHandlerCommunication, Communication {

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    boolean isLoggedIn = false;
    private Thread notifiListener, handelingCommands;


    ClientHandler(Socket socket) {

        this.socket = socket;
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("error in getting streams");
        }

        Runnable r1 = this::notificationListener;
        Runnable r2 = this::commandHandler;
        notifiListener = new Thread(r1);
        handelingCommands = new Thread(r2);
        notifiListener.start();
        handelingCommands.start();

        /*
          <code>this::notificationListener</code> is equals to below code
               <code>
                   new Runnable() {
                      public void run() {
                          notificationListener();
                      }
                  };
               </code>
         */

    }


    @Override
    public void notificationListener() {

    }

    @Override
    public void commandHandler() {

        String command;

        try {
            while (true) {
                command = dataInputStream.readUTF();
                switch (command) {
                    case "login":
                        login();
                        break;
                    case "signuo":
                    case "sign up":
                    case "register":
                        signup();
                        break;
                    default:
                        unknownCommand();
                        break;
                }
            }
        } catch (IOException ioe) {
            System.err.println("error in command handler for socket with channel : " + socket.getChannel()
                    + ", and port number of : " + socket.getPort());
        }
    }

    @Override
    public void login() {

    }

    @Override
    public void signup() {

    }

    @Override
    public void unknownCommand() {

    }
}
