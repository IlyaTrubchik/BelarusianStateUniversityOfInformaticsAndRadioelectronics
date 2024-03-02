import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Optional;

public class MenuController {
    @FXML
    Button startGameButton;
    @FXML
    Button settingsButton;
    @FXML
    Button exitButton;
    @FXML
    Text nameText;
    @FXML
    private void handleStartGame(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("lobbyPage.fxml"));
        Parent gameRoot = fxmlLoader.load();
        Scene scene = startGameButton.getScene();
        scene.setRoot(gameRoot);
        GameApplication.currentController=  fxmlLoader.getController();
    }
    @FXML
    private void onChangeName(){
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Change Name");
        dialog.setHeaderText(null);
        dialog.setContentText("Enter new Name:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(text -> {nameText.setText("Name: "+text);changeName(text);});
    }
    @FXML
    public void changeName(String name)
    {
        GameApplication.hostPlayer.setPlayerName(name);
        updateName(name);
    }
    @FXML
    private void handleExitGame(){
        Stage stage =((Stage)exitButton.getScene().getWindow());
        stage.close();
    }
    @FXML
    public void updateName(String Name){
        nameText.setText("Name:"+Name);
    }
}