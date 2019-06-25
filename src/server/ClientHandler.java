package server;

import constants.Constants;
import org.bson.Document;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {

    Socket client;
    DataOutputStream print;
    DataInputStream scan;
    User user;

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

                System.out.println(command);

                switch(command) {
                    case Constants.CHATMASSAGE:
                        newMassage(doc);
                        break;
                    case Constants.LOGINREQUEST:
                        login(doc);
                        break;
                    case Constants.SIGNUPREQUEST:
                        signUp(doc);
                        break;
                    case Constants.NOTIFICATION:

                        break;
                    case Constants.COMPLETEPROFILE:
                        completeProfile(doc);
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

    private void completeProfile(Document doc) {
        String username = doc.getString(Constants.USERNAME);
        doc.remove(Constants.USERNAME);

        Connection.updateADocument("User", Constants.USERNAME, username, doc);
    }

    private void newMassage(Document doc) { // how to save chats ???!!!
//        Document document = new Document(); // use for sending as notification
//        document.put(Constants.TYPE, Constants.NOTIFICATION);

        String toUser = doc.getString(Constants.TOUSER);

    }

    private void signUp(Document doc) {
        Document result = new Document();
        result.put(Constants.TYPE, Constants.SIGNUPREQUEST);

        String username = doc.getString(Constants.USERNAME);
        String password = doc.getString(Constants.PASSWORD);

        if (Connection.isDocumentInCollection("User", Constants.USERNAME, username)) {
            result.put(Constants.WASSUCCESS, false);
            result.put(Constants.DESCRIPTION, "user already existed");
        } else {
            doc.remove(Constants.TYPE);
            Connection.addADocument("User", doc);

            result.put(Constants.WASSUCCESS, true);
        }

        try {
            print.writeUTF(result.toJson());
            print.flush();
        } catch (IOException e) {
            e.printStackTrace();
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
                result.put(Constants.WASSUCCESS, true);
            } else {
                result.put(Constants.WASSUCCESS, false);
                result.put(Constants.DESCRIPTION, "Wrong Password");
            }
        } else {
            result.put(Constants.WASSUCCESS, false);
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