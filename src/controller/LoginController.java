package controller;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.Main;
import model.PageLoader;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;

public class LoginController {

    @FXML
    private TextField usernameText;

    @FXML
    private PasswordField passwordText;

    @FXML
    private TextField showPassText;

    @FXML
    private Text alertMassage;

    @FXML
    void enterWorkPlace() throws IOException {

        String username = usernameText.getText();
        String password = passwordText.getText();

        MongoClient mongoClient = new MongoClient();
        MongoDatabase database = mongoClient.getDatabase("miniTweeter");
        MongoCollection<Document> collection = database.getCollection("Users");

        Document doc = collection.find(eq("username", username)).first();

        if (doc != null) {
            if (doc.getString("password").equals(password)) {
                loginUser(doc);
                new PageLoader().load("/view/workPlace.fxml");
            } else {
                // wrong password
                alertMassage.setText("Wrong password");
                alertMassage.setVisible(true);
            }
        } else {
            // user does not exist
            alertMassage.setText("User does not exist");
            alertMassage.setVisible(true);
        }

    }

    private void loginUser(Document doc) {

    }

    @FXML
    void hidePass() {
        passwordText.setVisible(true);
        passwordText.setText(showPassText.getText());
        showPassText.setVisible(false);
    }

    @FXML
    void loadSignUpPage() throws IOException {
        new PageLoader().load("/view/signup_page.fxml");
    }

    @FXML
    void showPass() {
        showPassText.setVisible(true);
        showPassText.setText(passwordText.getText());
        passwordText.setVisible(false);
    }
}
