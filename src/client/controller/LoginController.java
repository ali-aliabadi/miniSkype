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
import java.net.ConnectException;

import static client.controller.UsefulFunctions.errorAlert;

public class LoginController {

    @FXML
    private TextField usernameText;

    @FXML
    private PasswordField passwordText;

    @FXML
    private Label passLabel;


    @FXML
    void signIn() throws IOException {
        String userName = usernameText.getText();
        String password = passwordText.getText();

        Document request = new Document();
        request.append(Constants.TYPE, Constants.LOGINREQUEST);
        request.append(Constants.USERNAME, userName);
        request.append(Constants.PASSWORD, password);

        if (userName.replace("", "").equals("") || password.replace(" ", "").equals("")) {
            errorAlert("ارور", "ابتدا جا های خالی را کامل کنید");
        } else {

            Connection.print.writeUTF(request.toJson());
            Connection.print.flush();

            String resultJson = Connection.scan.readUTF();

            Document result = Document.parse(resultJson);

            if (result.get(Constants.TYPE).equals(Constants.LOGINREQUEST)) {
                if (result.getBoolean(Constants.WASSUCCESS)) {
                    new PageLoader().load("/client/view/main.fxml");
                } else {
                    errorAlert("ارور در وارد شدن", result.getString(Constants.DESCRIPTION));
                }
            }
        }
    }

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
    void signUp() {
        try {
            new PageLoader().load("/client/view/Signup.fxml");
        } catch (IOException e) {
            System.err.println("error loading signup page");
        }
    }

    @FXML
    void backHome() {
        try {
            new PageLoader().load("/client/view/home.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
