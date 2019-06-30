package client.controller;

import client.Connection;
import client.model.PageLoader;
import constants.Constants;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import org.bson.Document;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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

        thread = new Thread(this::getMassage);
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
                        Platform.runLater(() -> newNotification(message));
                        break;
                    case Constants.NOTIFICATIONANS:
                        Platform.runLater(() -> errorAlert("مشکل در زنگ زدن", message.getString(Constants.DESCRIPTION)));
                        break;
                    case Constants.CALL:
                        Platform.runLater(() -> calling(message));
                        break;
                    case Constants.VOICECALL:
                        voiceCall(message);
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

    private void voiceCall(Document message) { //ip    //port
        try {
            Socket callSocket = new Socket(message.getString(Constants.IP), (Integer) message.get(Constants.PORT));
            VoiceStream voiceStream = new VoiceStream(new DataInputStream(callSocket.getInputStream()), new DataOutputStream(callSocket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addCall(Document message) {
        int len = Integer.parseInt(message.getString(Constants.NUMOFCHATS));
        for (int i = 0; i < len; i++) {
            String str = message.getString(String.valueOf(i));
            DialogPane dp = new DialogPane();
            dp.setHeaderText(str);
            dp.setContentText("  ");
            dp.setPadding(new Insets(20, 10, 10, 20));
            dp.setStyle("-fx-background-color :#ccc; -fx-border-width : 0px 0px 1px 0px; -fx-border-color : #aaa");
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
