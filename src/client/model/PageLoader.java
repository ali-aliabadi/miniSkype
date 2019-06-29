package client.model;


import client.Connection;
import constants.Constants;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.bson.Document;

import java.io.IOException;

public class PageLoader {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 470;
    private static Stage stage;

    static void intiStage(Stage primarySrage) {
        stage = primarySrage;
        stage.setTitle("Skype");
        stage.setResizable(false);
        stage.setWidth(WIDTH);
        stage.setHeight(HEIGHT);
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Document doc = new Document();
                doc.append(Constants.TYPE, Constants.EXIT);
                try {
                    Connection.print.writeUTF(doc.toJson());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void load (String url) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(url));
        stage.setScene(new Scene(root, WIDTH, HEIGHT));
        stage.show();
    }

}
