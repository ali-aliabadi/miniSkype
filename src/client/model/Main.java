package client.model;

import client.Connection;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        PageLoader.intiStage(primaryStage);
        new PageLoader().load("/client/view/home.fxml");
    }


    public static void main(String[] args) throws IOException {
//        Connection connection = new Connection("localhost", 8888);
        launch(args);
    }
}
