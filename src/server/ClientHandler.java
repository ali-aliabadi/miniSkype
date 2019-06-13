package server;

import org.bson.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.Socket;

public class ClientHandler extends Thread {

    Socket client;
    DataOutputStream print;
    DataInputStream scan;

    public ClientHandler(Socket client) {
        this.client=client;
        try {
            print = new DataOutputStream(client.getOutputStream());
            scan = new DataInputStream(client.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        start();
    }

    @Override
    public void run() {
        String command;
        while (true) {
            try {
                command = scan.readUTF();


                Document doc = Document.parse(command);
                System.out.println(doc);
                System.out.println(doc.toJson());

                break;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}