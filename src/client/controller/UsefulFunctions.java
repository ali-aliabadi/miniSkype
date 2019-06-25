package client.controller;

import javafx.scene.control.Alert;

public class UsefulFunctions {

    static void errorAlert(String title, String contentText) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(contentText);
        a.show();
    }
}
