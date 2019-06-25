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

    @FXML
    void signIn() {
        String username = usernameText.getText();
        String password = passwordText.getText();

        Document doc = new Document();
        doc.append(Constants.TYPE, Constants.LOGINREQUEST);
        doc.append(Constants.USERNAME, username);
        doc.append(Constants.PASSWORD, password);

        System.out.println(doc.toJson());

        try {
            Connection.print.writeUTF(doc.toJson());
            Connection.print.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document result = null;
        try {
            String resultInJson = Connection.scan.readUTF();
            result = Document.parse(resultInJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (result.get(Constants.WASSUCCESS).equals(true)) {

        } else {
            errorAlert("loginError",result.getString(Constants.DESCRIPTION));
        }
    }

}
