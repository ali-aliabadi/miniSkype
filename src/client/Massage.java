package client;

public class Massage {

    private CommandsType type;

    // login / sign up fields
    private String username;
    private String password;

    public Massage(CommandsType type) {
        this.type = type;
    }

    public Massage() {
    }

    public CommandsType getType() {
        return type;
    }

    public void setType(CommandsType type) {
        this.type = type;
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
}
