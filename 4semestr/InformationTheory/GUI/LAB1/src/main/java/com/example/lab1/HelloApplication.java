package com.example.lab1;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;

public class HelloApplication extends Application {
    public static Stage stage;
    public static Alert alert;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 553, 466);
        stage.setTitle("CypherProgram");
        HelloApplication.stage =stage;

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setX(550);
        alert.setY(230);
        HelloApplication.alert = alert;
        alert.setTitle("Error");
        alert.setContentText("Please enter message and key");
        stage.setScene(scene);
        stage.show();
    }


    public static String text="";
    public static String key="";
    public static int mode = 0;
    public static void main(String[] args) {
        launch();
    }
}