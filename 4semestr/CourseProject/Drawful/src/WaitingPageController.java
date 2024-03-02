import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class WaitingPageController {
    @FXML
    Button  startGame;
    @FXML
    Label waitLabel;
    @FXML
    private void handleStartGame() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("drawingPage.fxml"));
        Parent parent = fxmlLoader.load();
        Scene scene = startGame.getScene();
        GameApplication.currentController = fxmlLoader.getController();
        scene.setRoot(parent);
        GameApplication.server.startGame();
    }
    public void playersChangeScreen() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("drawingPage.fxml"));
        Parent parent = fxmlLoader.load();
        Scene scene = waitLabel.getScene();
        GameApplication.currentController = fxmlLoader.getController();
        scene.setRoot(parent);
    }
    public void setStartable(){
        startGame.setDisable(false);
    }

}
