package client.model;

import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        PageLoader.intiStage(primaryStage);
        new PageLoader().load("/client/view/Login.fxml");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
