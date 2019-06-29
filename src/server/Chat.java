package server;

import java.util.ArrayList;

public class Chat {

    String user1;
    String user2;
    private int chatId;
    ArrayList<Message> messages = new ArrayList<>();

    Chat() {
        synchronized (Server.chatId) {
            chatId = Server.chatId++;
        }
    }


}
