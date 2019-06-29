package client.controller;

import client.Connection;
import client.model.PageLoader;
import constants.Constants;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.bson.Document;

import java.io.IOException;
import java.util.Optional;

import static client.controller.UsefulFunctions.errorAlert;

public class MainController {

    @FXML
    private VBox vboxChats;

    @FXML
    private VBox vboxContacts;

    @FXML
    private VBox vboxChat;

    @FXML
    private Label welcomeLabel;

    @FXML
    private VBox vboxProfile;

    @FXML
    private HBox hboxMessage;

    private Thread thread;

    @FXML
    private ScrollPane scrollChats;

    @FXML
    private ScrollPane scrollMessage;

    @FXML
    private ScrollPane scrollContacts;

    private String userSpeak;

    @FXML
    private Label userSpeakLabel;

    @FXML
    void call() {
        Document doc = new Document();
        doc.append(Constants.TYPE, Constants.CALL);
        doc.append(Constants.TOUSER, userSpeak);

        try {
            Connection.print.writeUTF(doc.toJson());
            Connection.print.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void logOut() {
        thread.interrupt();
        try {
            Connection.print.writeUTF(new Document().append(Constants.TYPE, Constants.LOGOUT).toJson());
            new PageLoader().load("/client/view/Login.fxml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void search() {

    }


    @FXML
    void showChats() {
        scrollChats.setVisible(true);
        vboxChats.setVisible(true);
        scrollContacts.setVisible(false);
        vboxContacts.setVisible(false);
    }

    @FXML
    void showContacts() {
        scrollContacts.setVisible(true);
        vboxContacts.setVisible(true);
        scrollChats.setVisible(false);
        vboxChats.setVisible(false);
    }

    @FXML
    void showProfile() {
        scrollMessage.setVisible(false);
        hboxMessage.setVisible(false);
        vboxProfile.setVisible(true);
    }

    @FXML
    void initialize() {
        Document doc = new Document();
        doc.append(Constants.TYPE, Constants.INITMAIN);

        try {
            Connection.print.writeUTF(doc.toJson());
            Connection.print.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                getMassage();
            }
        });
        thread.start();

        showProfile();
        showChats();
    }

    private void getMassage() {
        try {
            while (true) {
                Document message = Document.parse(Connection.scan.readUTF());
                switch (message.getString(Constants.TYPE)) {
                    case Constants.CONTACTSRESULT:
                        addContacts(message);
                        break;
                    case Constants.PROFILE:
                        addProfile(message);
                        break;
                    case Constants.CHATRESULT:
                        addCall(message);
                        break;
                    case Constants.NOTIFICATION:
                        newNotification(message);
                        break;
                    case Constants.NOTIFICATIONANS:
                        errorAlert("مشکل در زنگ زدن", message.getString(Constants.DESCRIPTION));
                        break;
                    case Constants.CALL:
                        calling(message);
                        break;

                }
            }
        } catch (IOException e) {
            System.err.println("error getting message");
        }
    }

    private void calling(Document doc) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("تماس");
        alert.setContentText(null);
        alert.setHeaderText("تماس برقرار شد");

    }

    private void newNotification(Document message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("یک تماس جدید");
        alert.setHeaderText("تماس جدیدی از طرف " + message.getString(Constants.FROMUSER) + "دارید");
        alert.setContentText("انتخاب کنید");

        ButtonType buttonTypeOne = new ButtonType("جواب دادن", ButtonBar.ButtonData.YES);
        ButtonType buttonTypeTwo = new ButtonType("رد کردن", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
        Optional<ButtonType> result = alert.showAndWait();
        Document doc = new Document();
        doc.append(Constants.TYPE, Constants.NOTIFICATIONANS);
        if (result.get() == buttonTypeOne) {
            doc.append(Constants.ACCEPTED, Constants.YES);
            voiceCall();
        } else {
            doc.append(Constants.ACCEPTED, Constants.NO);
        }

        try {
            Connection.print.writeUTF(doc.toJson());
            Connection.print.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void voiceCall() {

    }

    private void addCall(Document message) {
        int len = Integer.parseInt(message.getString(Constants.NUMOFCHATS));
        for (int i = 0; i < len; i++) {
            String str = message.getString(String.valueOf(i));
            HBox hBox = new HBox();
//            ImageView imageView = new ImageView();
//            imageView.setFitHeight(30);
//            imageView.setFitWidth(30);
//            imageView.setImage(new Image("res/ProfileLogo.png"));
            Label label = new Label(str.substring(str.indexOf('?') + 1));
            label.setFont(new Font(15));
//            hBox.getChildren().add(imageView);
            hBox.getChildren().add(label);
//            HBox.setMargin(imageView, new Insets(15,10,10,10));
            HBox.setMargin(label, new Insets(28, 0, 0, 15));
        }
    }

    private void addProfile(Document message) {
        welcomeLabel.setText(welcomeLabel.getText() + message.getString(Constants.USERNAME));
    }

    private void addContacts(Document message) {
        int len = Integer.parseInt(message.getString(Constants.NUMOFCONTACTS));
        for (int i = 0; i < len; i++) {
            ContactView contactView = new ContactView();
            contactView.dp.setHeaderText(message.getString(String.valueOf(i)));
            final int ii = i;
            contactView.button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent ActionEvent) {
                    userSpeak = message.getString(String.valueOf(ii));
                    userSpeakLabel.setText(userSpeak);
                    scrollMessage.setVisible(true);
                    vboxChat.setVisible(true);
                    hboxMessage.setVisible(true);
                    vboxProfile.setVisible(false);
                }
            });
            vboxContacts.getChildren().add(contactView.dp);
        }
    }
}
