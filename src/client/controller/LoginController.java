package client.controller;

import client.Connection;
import client.model.PageLoader;
import constants.Constants;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.bson.Document;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameText;

    @FXML
    private PasswordField passwordText;

    @FXML
    void login() throws IOException {
        String userName = usernameText.getText();
        String password = passwordText.getText();

        Document request = new Document();
        request.put(Constants.TYPE, Constants.LOGINREQUEST);
        request.put(Constants.USERNAME, userName);
        request.put(Constants.PASSWORD, password);

        if (userName.replace("", "").equals("") || password.replace(" ", "").equals("")) {
            errorAlert("Error", "please fill the blanks first...");
        } else {
            Connection.print.writeUTF(request.toJson());
            Connection.print.flush();

            Document result = Document.parse(Connection.scan.readUTF());

            if (result.get(Constants.TYPE).equals(Constants.LOGINREQUEST)) {
                if (result.getBoolean(Constants.WASSUCCESS)) {
                    new PageLoader().load("/view/workPlace.fxml");
                } else {
                    errorAlert("Error in user authentication", result.getString(Constants.DESCRIPTION));
                }
            }
        }
    }

    void errorAlert(String title, String contentText) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(title);
        a.setHeaderText(null);
        a.setContentText(contentText);
        a.show();
    }

}
