package server;

import constants.Constants;
import org.bson.Document;

import java.util.ArrayList;

class User {

    String username;
    String password;
    ArrayList<String> chatsId = new ArrayList<>();

    static User parse(Document doc) {
        User user = new User();
        user.username = doc.getString(Constants.USERNAME);
        user.password = doc.getString(Constants.PASSWORD);
        user.chatsId = (ArrayList<String>) doc.get(Constants.CHATSID);
        if (user.chatsId == null) {
            user.chatsId = new ArrayList<String>();
        }
        return user;
    }

}
