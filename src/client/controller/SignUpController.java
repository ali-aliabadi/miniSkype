package client.controller;

import client.Connection;
import client.model.PageLoader;
import constants.Constants;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.bson.Document;

import java.io.IOException;

import static client.controller.UsefulFunctions.errorAlert;

public class SignUpController {

    @FXML
    private Label passLabel;

    @FXML
    private TextField usernameText;

    @FXML
    private PasswordField passwordText;

    @FXML
    private PasswordField passwordText1;

    @FXML
    private Label confirmPassLabel;

    @FXML
    void hidePass() {
        passLabel.setVisible(false);
        passwordText.setVisible(true);
    }

    @FXML
    void showPass() {
        passLabel.setText(passwordText.getText());
        passLabel.setVisible(true);
        passwordText.setVisible(false);
    }

    @FXML
    void backToSignIn() {
        try {
            new PageLoader().load("/client/view/Login.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    void hideConfirmPass() {
        confirmPassLabel.setVisible(false);
        passwordText1.setVisible(true);
    }

    @FXML
    void showConfirmPass() {

        confirmPassLabel.setText(passwordText1.getText());
        passwordText1.setVisible(true);
        passwordText1.setVisible(false);
    }

    @FXML
    void signUp() {
        String username = usernameText.getText();
        String password = passwordText.getText();
        String confirmPassword = confirmPassLabel.getText();

        if(password.equals(confirmPassword)) {
            Document doc = new Document();
            doc.append(Constants.TYPE, Constants.SIGNUPREQUEST);
            doc.append(Constants.USERNAME, username);
            doc.append(Constants.PASSWORD, password);

            Document result = null;
            try {
                Connection.print.writeUTF(doc.toJson());
                Connection.print.flush();
                result = Document.parse(Connection.scan.readUTF());
            } catch (IOException e) {
                e.printStackTrace();
            }

            if ((boolean) result.get(Constants.WASSUCCESS)) {

            } else {
                errorAlert("signUp error", result.getString(Constants.DESCRIPTION));
            }
        } else {
            errorAlert("signUp error", "password and confirm password didnt match");
        }
    }

}
