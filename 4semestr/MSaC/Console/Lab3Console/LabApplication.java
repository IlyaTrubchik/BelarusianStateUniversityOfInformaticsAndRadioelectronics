
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class LabApplication extends Application {
    public static Stage mainStage;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LabApplication.class.getResource("application.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 747, 563);
        stage.setTitle("Hello!");
        mainStage = stage;
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
