package client;

import java.util.ArrayList;

public class Massage {

    private boolean isNotification;
    private String command;

    private int testInt;
    private ArrayList<Long> testArrayListOfLonges = new ArrayList<>();

    private CommandsType commandType;

    private String testString;

    public Massage() {

    }

    public Massage(boolean isNotification, String command, int testInt, CommandsType commandType) {
        this.isNotification = isNotification;
        this.command = command;
        this.testInt = testInt;
        this.commandType = commandType;
    }

    public boolean isNotification() {
        return isNotification;
    }

    public void setNotification(boolean notification) {
        isNotification = notification;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public int getTestInt() {
        return testInt;
    }

    public void setTestInt(int testInt) {
        this.testInt = testInt;
    }

    public ArrayList<Long> getTestArrayListOfLonges() {
        return testArrayListOfLonges;
    }

    public void setTestArrayListOfLonges(ArrayList<Long> testArrayListOfLonges) {
        this.testArrayListOfLonges = testArrayListOfLonges;
    }

    public CommandsType getCommandType() {
        return commandType;
    }

    public void setCommandType(CommandsType commandType) {
        this.commandType = commandType;
    }

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }
}
