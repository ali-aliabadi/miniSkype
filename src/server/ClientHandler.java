package server;

import constants.Constants;
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
                command = doc.getString(Constants.TYPE);

                switch(command) {
                    case Constants.CHATMASSAGE:

                        break;
                    case Constants.LOGINREQUEST:
                        login(doc);
                        break;
                    case Constants.SIGNUPREQUEST:

                        break;
                    case Constants.NOTIFICATION:

                        break;
                    default:

                        break;
                }

                break;

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.gc();
        }
    }

    private void login(Document doc) {
        String username = doc.getString(Constants.USERNAME);
        String password = doc.getString(Constants.PASSWORD);

        Document result = new Document();
        result.put(Constants.TYPE, Constants.LOGINREQUEST);

        if(Connection.isDocumentInCollection("User", Constants.USERNAME, username)) {
            String userPass = (String) Connection.getValueOfADocumentInCollection("User", Constants.USERNAME, username, Constants.PASSWORD);
            if (password.equals(userPass)) {
                result.put("wasSuccess", true);
            } else {
                result.put("wasSuccess", false);
                result.put(Constants.DESCRIPTION, "Wrong Password");
            }
        } else {
            result.put("wasSuccess", true);
            result.put(Constants.DESCRIPTION, "User not Existed");
        }

        try {
            print.writeUTF(result.toJson());
            print.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}