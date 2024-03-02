import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientApplication.class.getResource("client.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 569, 379);
        primaryStage.setResizable(false);
        primaryStage.setTitle("HttpClient");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
