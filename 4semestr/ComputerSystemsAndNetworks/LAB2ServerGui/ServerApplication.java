import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class ServerApplication extends Application {
    public static Controller controller;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ServerApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 647, 459);
        ServerApplication.controller = fxmlLoader.getController();
        stage.setTitle("Hello!");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(windowEvent -> {
            controller.close();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}