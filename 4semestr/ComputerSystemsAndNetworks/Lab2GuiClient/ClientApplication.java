import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApplication extends Application {
    public static Controller controller;
    public static Stage stage;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 647, 459);
        controller = fxmlLoader.getController();
        ClientApplication.stage = stage;
        stage.setTitle("Client");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        stage.setOnCloseRequest(windowEvent -> {
            if(controller.client!=null && Client.isConnected ==true)
           controller.client.disconnect();
        });
    }

    public static void main(String[] args) {
        launch();
    }
}