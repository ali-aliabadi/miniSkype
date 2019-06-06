package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * methods need in client class to communicate with clienthandler
 *  commandreader : read command from user and sending to client (handling command)
 *  login : (must have a pair method in clienthandler)
 *  signup : (must have a pair method in clienthandler)
 */
public interface ClientCommunication {


    void commandReader() throws IOException;

}
