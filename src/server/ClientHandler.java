package server;

import com.mongodb.client.MongoCollection;
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
    ClientHandler callCH;

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

                doc.remove(Constants.TYPE);

                switch(command) {
                    case Constants.CHATMASSAGE:
                        newMassage(doc);
                        break;
                    case Constants.LOGINREQUEST:
                        login(doc);
                        break;
                    case Constants.LOGOUT:
                        logOut();
                        break;
                    case Constants.SIGNUPREQUEST:
                        signUp(doc);
                        break;
                    case Constants.NOTIFICATION:
                        newNotification(doc);
                        break;
                    case Constants.COMPLETEPROFILE:
                        completeProfile(doc);
                        break;
                    case Constants.INITMAIN:
                        initMain();
                        break;
                    case Constants.GETMESSAGES:
                        getMessage(doc);
                        break;
                    case Constants.CALL:
                        newCall(doc);
                        break;
                    case Constants.EXIT:
                        exiting();
                        break;
                    case Constants.NOTIFICATIONANS:
                        nofivationAnswer(doc);
                        break;
                    default:
                        System.err.println("\nbad type sending from client\n\t" + command + "\n");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
            System.gc();
        }
    }

    private void nofivationAnswer(Document doc) {
        Document result = new Document();
        if (doc.getString(Constants.ACCEPTED).equals(Constants.NO)) {
            result.append(Constants.TYPE, Constants.NOTIFICATIONANS);
            result.append(Constants.DESCRIPTION, "کاربر جواب نمی دهد");
            synchronized (callCH.print) {
                try {
                    callCH.print.writeUTF(result.toJson());
                    callCH.print.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            result.append(Constants.TYPE, Constants.CALL);
            result.append(Constants.IP, "192.168.1.33");
            result.append(Constants.PORT, 12012);
            try {
                print.writeUTF(result.toJson());
                print.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            synchronized (callCH.print) {
                try {
                    callCH.print.writeUTF(result.toJson());
                    callCH.print.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void newCall(Document doc) {
        String touser = doc.getString(Constants.TOUSER);
        synchronized (Server.clientHandlers) {
            for (ClientHandler ch : Server.clientHandlers) {
                if (ch.user.username.equals(touser)) {
                    callCH = ch;
                    sendNotification(user.username, ch);
                    return;
                }
            }
        }
        Document result = new Document();
        result.append(Constants.TYPE, Constants.NOTIFICATIONANS);
        result.append(Constants.DESCRIPTION, "یوزر افلاین است");

        try {
            print.writeUTF(result.toJson());
            print.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(String fromuser, ClientHandler touser) {
        Document doc = new Document();
        doc.append(Constants.TYPE, Constants.NOTIFICATION);
        doc.append(Constants.FROMUSER, fromuser);
        synchronized (touser.print) {
            try {
                touser.print.writeUTF(doc.toJson());
                touser.print.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void logOut() {
        this.user = null;
    }

    private void exiting() {
        synchronized (Server.clientHandlers) {
            Server.clientHandlers.remove(this);
        }
    }

    private void newNotification(Document doc) {
        String fromUserName = doc.getString(Constants.FROMUSER);
        String toUser = doc.getString(Constants.TOUSER);

        // sending notification to user if he is online.
        synchronized (Server.clientHandlers) {
            for (ClientHandler ch : Server.clientHandlers) {
                if (toUser.equals(ch.user.username)) {
                    doc.append(Constants.TYPE, Constants.NOTIFICATION);
                    break;
                }
            }
        }

        // add this notification to mongo

    }

    private void getMessage(Document doc) {
        String chatId = doc.getString(Constants.CHATID);
        Document result = Connection.getFirstDocument(Constants.CHATS, Constants.CHATID, chatId);
        result.append(Constants.TYPE, Constants.GETMESSAGES);
        try {
            print.writeUTF(result.toJson());
        } catch (IOException e) {
            System.out.println(result.toJson());
            e.printStackTrace();
        }
    }

    private void initMain() {
        Document contactsResult = new Document();
        Document profileResult = new Document();
        Document chatsResult = new Document();

        contactsResult.append(Constants.TYPE, Constants.CONTACTSRESULT);
        MongoCollection<Document> col = Connection.getCollection("User");
        int len = (int) (col.count() - 1);
        contactsResult.append(Constants.NUMOFCONTACTS, String.valueOf(len));
        int j = 0;
        for(Document d : col.find()) {
            if (! d.getString(Constants.USERNAME).equals(user.username)) {
                contactsResult.append(String.valueOf(j ++), d.getString(Constants.USERNAME));
            }
        }

        profileResult.append(Constants.TYPE, Constants.PROFILE);
        profileResult.append(Constants.USERNAME, user.username);

        chatsResult.append(Constants.TYPE, Constants.CHATRESULT);
        len = user.chatsId.size();
        chatsResult.append(Constants.NUMOFCHATS, String.valueOf(len));
        for(int i = 0; i < len; i++) {
            String str;
            Document doc = Connection.getFirstDocument(Constants.CHATS, Constants.CHATID, user.chatsId.get(i));
            if (doc.getString("user1").equals(user.username)) {
                str = doc.getString("user2");
            } else {
                str = doc.getString("user1");
            }
            contactsResult.append(String.valueOf(i), user.chatsId.get(i) + '?' + str);
        }

        try {
            print.writeUTF(contactsResult.toJson());
            print.flush();
            print.writeUTF(profileResult.toJson());
            print.flush();
            print.writeUTF(chatsResult.toJson());
            print.flush();
        } catch (IOException e) {
            e.printStackTrace();
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
        result.append(Constants.TYPE, Constants.SIGNUPREQUEST);

        String username = doc.getString(Constants.USERNAME);

        if (Connection.isDocumentInCollection("User", Constants.USERNAME, username)) {
            result.append(Constants.WASSUCCESS, false);
            result.append(Constants.DESCRIPTION, "نام کاربری قبلا گرفته شده");
        } else {
            Connection.addADocument("User", doc);
            result.append(Constants.WASSUCCESS, true);
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
        result.append(Constants.TYPE, Constants.LOGINREQUEST);

        if(Connection.isDocumentInCollection("User", Constants.USERNAME, username)) {
            String userPass = (String) Connection.getValueOfADocumentInCollection("User", Constants.USERNAME, username, Constants.PASSWORD);
            if (password.equals(userPass)) {
                result.append(Constants.WASSUCCESS, true);
                setUser(username);
            } else {
                result.append(Constants.WASSUCCESS, false);
                result.append(Constants.DESCRIPTION, "پسورد اشتباه");
            }
        } else {
            result.append(Constants.WASSUCCESS, false);
            result.append(Constants.DESCRIPTION, "کاربر وجود ندارد");
        }

        try {
            print.writeUTF(result.toJson());
            print.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUser(String username) {
        Document doc = Connection.getFirstDocument("User", Constants.USERNAME, username);
        this.user = User.parse(doc);
    }

}