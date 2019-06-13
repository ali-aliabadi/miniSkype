package client.controller;


import client.model.PageLoader;
import javafx.fxml.FXML;

import java.io.IOException;

public class HomeController {

    @FXML
    void goToLoginPage() throws IOException {
        new PageLoader().load("/client/view/Login.fxml");
    }

}
