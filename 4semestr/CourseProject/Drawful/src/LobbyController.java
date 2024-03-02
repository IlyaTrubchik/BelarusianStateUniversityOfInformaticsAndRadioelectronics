import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class LobbyController {
    @FXML
    Button createLobbyButton;
    @FXML
    Button settingsButton;
    @FXML
    Button backButton;
    @FXML
    Button enterLobbyButton;
    @FXML
    Button enterButton;
    @FXML
    TextField portTextField;
    @FXML
    TextField ipTextField;
    @FXML
    AnchorPane mainPane;

    @FXML
    private void handleBackButton(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader =new FXMLLoader(GameApplication.class.getResource("menu.fxml"));
        Parent parent = fxmlLoader.load();
        Scene scene = backButton.getScene();
        scene.setRoot(parent);
        GameApplication.currentController =fxmlLoader.getController();
    }
    @FXML
    private void enterInLobby(){
        try {
            GameApplication.isStarted = true;
            Socket socket = new Socket(ipTextField.getText(), Integer.parseInt(portTextField.getText()));
            GameApplication.player = new clientPlayer(socket, GameApplication.hostPlayer.getName());
            GameApplication.player.sendMessage("/name" + GameApplication.hostPlayer.getName());
            FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("waitingConnectionsPage.fxml"));
            Parent parent = fxmlLoader.load();
            Scene scene = enterButton.getScene();
            GameApplication.currentController = fxmlLoader.getController();
            scene.setRoot(parent);
        }
        catch(IOException e)
        {
            GameApplication.isStarted = false;
            if(mainPane!=null)
            {
                GameApplication.messageBox.windowPane.toFront();
                GameApplication.messageBox.setText("Cant connect to server");
                GameApplication.messageBox.render();
            }
        }
        catch (NumberFormatException exc)
        {
            if(mainPane!=null)
            {
                GameApplication.messageBox.windowPane.toFront();
                GameApplication.messageBox.setText("Invalid port value");
                GameApplication.messageBox.render();
            }
        }
    }
    @FXML
    private void handleEnterLobby(ActionEvent event) throws  IOException{
        FXMLLoader fxmlLoader = new FXMLLoader((GameApplication.class.getResource("enterLobbyPage.fxml")));
        Parent parent = fxmlLoader.load();
        Scene scene = createLobbyButton.getScene();
        scene.setRoot(parent);
        GameApplication.currentController =fxmlLoader.getController();
    }

    @FXML
    private void handleSettingsClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader((GameApplication.class.getResource("lobbySettings.fxml")));
        Parent parent = fxmlLoader.load();
        Scene scene = createLobbyButton.getScene();
        scene.setRoot(parent);
        GameApplication.currentController =fxmlLoader.getController();
    }
    @FXML
    public void setHostWaitingPage(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader((GameApplication.class.getResource("waitingPage.fxml")));
            Parent parent = fxmlLoader.load();
            Scene scene = enterButton.getScene();
            scene.setRoot(parent);
            GameApplication.currentController = fxmlLoader.getController();
        }
        catch (IOException e)
        {
            System.out.println("Cant load next page");
        }
    }
    @FXML
    private void setCreateLobbyPage(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader((GameApplication.class.getResource("createLobbyPage.fxml")));
            Parent parent = fxmlLoader.load();
            Scene scene = createLobbyButton.getScene();
            scene.setRoot(parent);
            GameApplication.currentController = fxmlLoader.getController();
        }
        catch (IOException e)
        {
            System.out.println("Cant set next page");
            e.printStackTrace();
        }
    }
    @FXML
    private void handleCreateLobby() {
        try {
            GameApplication.server = new Server(Integer.parseInt(portTextField.getText()));
            Server.images = new ArrayList<>();
            Server.readyCounter = 0;
            Server.currGuessingImg = 0;
            GameApplication.server.start();
            GameApplication.isStarted = true;
        }
        catch(NumberFormatException e)
        {
            if(mainPane!=null)
            {
                GameApplication.messageBox.windowPane.toFront();
                GameApplication.messageBox.setText("Invalid port value");
                GameApplication.messageBox.render();
            }

        }
    }
    public void initialize(){
        if(mainPane!=null)
        {
            mainPane.getChildren().add(GameApplication.messageBox.windowPane);
            GameApplication.messageBox.windowPane.toFront();
        }
    }
}
