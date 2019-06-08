package client;

import constants.Communication;
import constants.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread implements ClientCommunication, Communication {

    private Socket socket;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;
    private Scanner jin;


    Client(String address, int port) throws IOException {

        socket = new Socket(address, port);
        dataInputStream = new DataInputStream(socket.getInputStream());
        dataOutputStream = new DataOutputStream(socket.getOutputStream());

        jin = new Scanner(System.in);

        start();
    }

    /* unused methods start*/

    @Override
    public void commandReader() {

    }

    @Override
    public void login() {

    }

    @Override
    public void signup() {

    }

    /* unused methods end*/

    @Override
    public void run() {

        String command;

        while (true) {
            System.out.println('*' * Constants.STARS_NUM_SEP);
            System.out.print("please input your command : ");
            try {
                dataOutputStream.writeUTF(jin.nextLine());
            } catch (IOException e) {
                System.err.println("error in sending command to server");
            }
            commandProcess:
            while (true) {
                try {
                    command = dataInputStream.readUTF();

                    switch (command) {
                        case Constants.NEED_INPUT:
                            dataOutputStream.writeUTF(jin.nextLine());
                            break;
                        case Constants.END_OF_PROCESS:
                            break commandProcess;
                        case Constants.NOTIFICATION:

                            break;
                        default:
                            System.out.println(command);
                    }
                    
                } catch (IOException e) {
                    System.err.println("error in getting command from server and processing it");
                }
            }
        }
    }

    @Override
    public void notificationListener() {

    }

}
