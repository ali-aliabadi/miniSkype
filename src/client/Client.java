package client;

import constants.Communication;
import constants.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client implements ClientCommunication, Communication {

    private Socket commandsSocket, notficationSocket;
    private DataInputStream commandInput, notificationInput;
    private DataOutputStream commandOutput, notificationOutput;
    private Thread notifiListener, handelingCommands;
    private Scanner jin;


    Client(String address, int port) throws IOException {

        commandsSocket = new Socket(address, port);
        commandInput = new DataInputStream(commandsSocket.getInputStream());
        commandOutput = new DataOutputStream(commandsSocket.getOutputStream());

        notficationSocket = new Socket(address, port);
        notificationInput = new DataInputStream(notficationSocket.getInputStream());
        notificationOutput = new DataOutputStream(notficationSocket.getOutputStream());

        jin = new Scanner(System.in);

        Runnable r1 = this::notificationListener;
        Runnable r2 = this::commandReader;
        notifiListener = new Thread(r1);
        handelingCommands = new Thread(r2);
        notifiListener.start();
        handelingCommands.start();
    }


    @Override
    public void notificationListener() {

    }

    @Override
    public void login() {

    }

    @Override
    public void signup() {

    }

    @Override
    public void commandReader() {

        String command;

        while (true) {
            System.out.println('*' * 33);
            System.out.print("please input your command : ");
            try {
                commandOutput.writeUTF(jin.nextLine());
            } catch (IOException e) {
                System.err.println("error in sending command to server");
            }
            commandProcess:
            while (true) {
                try {
                    command = commandInput.readUTF();

                    switch (command) {
                        case Constants.NEED_INPUT:
                            commandOutput.writeUTF(jin.nextLine());
                            break;
                        case Constants.END_OF_PROCESS:
                            break commandProcess;
                        default:
                            System.out.println(command);
                    }
                    
                } catch (IOException e) {
                    System.err.println("error in getting command from server and processing it");
                }
            }
        }
    }
}
