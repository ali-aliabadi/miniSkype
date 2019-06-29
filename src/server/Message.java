package server;

import java.util.Date;

public class Message {

    String text;
    Date date;

    public Message(String text) {
        this.text = text;
        date = new Date();
    }

}
