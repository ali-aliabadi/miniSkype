package client.controller;

import client.CommandsType;
import client.Massage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameText;

    @FXML
    private PasswordField passwordText;

    @FXML
    void login() throws JsonProcessingException {
        System.out.println("in login");
        String userName = usernameText.getText();
        String password = passwordText.getText();
        if (userName.replace("", "").equals("") || password.replace(" ", "").equals("")) {

        } else {
            System.out.println("hi");
            Massage msg = new Massage(CommandsType.LOGIN);
            msg.setUsername(userName);
            msg.setPassword(password);

            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String json = ow.writeValueAsString(msg);

            System.out.println(json);
        }
    }

}
