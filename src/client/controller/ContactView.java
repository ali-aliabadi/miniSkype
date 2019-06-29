package client.controller;


import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.text.Font;

public class ContactView {

    Button button;
    DialogPane dp;

    ContactView() {
        dp = new DialogPane();
        button = new Button("چت");
        button.setPadding(new Insets(6, 10, 6, 10));
        button.setFont(new Font("B Koodak Regular", 18));
        dp.setContentText("  ");
        dp.setGraphic(button);
        dp.setPadding(new Insets(10, 10, 10, 10));
        dp.setStyle("-fx-background-color :#ccc; -fx-border-width : 0px 0px 1px 0px; -fx-border-color : #aaa");
    }
}
