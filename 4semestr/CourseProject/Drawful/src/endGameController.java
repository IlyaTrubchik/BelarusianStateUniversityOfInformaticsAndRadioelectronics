import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.ArrayList;

public class endGameController {
    @FXML
    ListView scoreTable;
    @FXML
    Button returnButton;

    @FXML
    private void menuClick() throws IOException {
        if(GameApplication.player!=null)
        {
            GameApplication.player.disable();
        }else
        {
            GameApplication.server.disable();
        }
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("menu.fxml"));
        Parent root =fxmlLoader.load();
        Scene scene = returnButton.getScene();
        GameApplication.currentController = fxmlLoader.getController();
        scene.setRoot(root);
    }

    public void initializeScoreTable(){
        ArrayList<String> scoreT = new ArrayList<>();
        for (PlayerInfo player:GameApplication.scoreMap) {
            StringBuilder sb  = new StringBuilder();
            sb.append("Name: ");
            sb.append(player.getName());
            sb.append("   ");
            sb.append("Score:");
            sb.append(player.getScore());
            scoreT.add(sb.toString());
        }
        ObservableList<String> table = FXCollections.observableArrayList(scoreT);
        scoreTable.setItems(table);
    }
}
