package server;

public class User {

    private String username;
    private String password;
    private boolean isOnline;


    User(String username, String password) {
        this.username = username;
        this.password = password;
        isOnline = true;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
