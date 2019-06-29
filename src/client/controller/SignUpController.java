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
        String confirmPassword = passwordText1.getText();

        Document doc = new Document();
        doc.append(Constants.TYPE, Constants.SIGNUPREQUEST);
        doc.append(Constants.USERNAME, username);
        doc.append(Constants.PASSWORD, password);

        if(password.equals(confirmPassword)) {
            if (username.replace("", "").equals("") || password.replace(" ", "").equals("")) {
                errorAlert("Error", "please fill the blanks first...");
            } else {

                Document result = null;
                try {
                    Connection.print.writeUTF(doc.toJson());
                    Connection.print.flush();
                    result = Document.parse(Connection.scan.readUTF());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if ((boolean) result.get(Constants.WASSUCCESS)) {
                    try {
                        new PageLoader().load("/client/view/main.fxml");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    errorAlert("ارور ثبت نام", result.getString(Constants.DESCRIPTION));
                }
            }
        } else {
            errorAlert("ارور ثبت نام", "گذرواژه ها مطابفت ندارند");
            System.out.println(password);
            System.out.println(confirmPassword);
        }
    }

}
