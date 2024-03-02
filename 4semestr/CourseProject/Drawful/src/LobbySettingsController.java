import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import jdk.jfr.Event;

import java.awt.*;
import java.io.IOException;

public class LobbySettingsController {

    @FXML
    ToggleButton twoLobbySizeButton;
    @FXML
    ToggleButton fourLobbySizeButton;
    @FXML
    ToggleButton eightLobbySizeButton;
    @FXML
    ToggleGroup membersToggleGroup;
    @FXML
    ChoiceBox languageChoiceBox;
    @FXML
    ToggleGroup drawingToggleGroup;
    @FXML
    ToggleGroup guessingToggleGroup;
    @FXML
    ToggleButton fifteenDrawing;
    @FXML
    ToggleButton thirtyDrawing;
    @FXML
    ToggleButton minuteDrawing;
    @FXML
    ToggleButton twoMinutesDrawing;
    @FXML
    ToggleButton fifteenGuessing;
    @FXML
    ToggleButton thirtyGuessing;
    @FXML
    ToggleButton minuteGuessing;
    @FXML
    ToggleButton twoMinutesGuessing;
    public static Settings settings =  new Settings();

    @FXML
    private void initialize(){
        twoLobbySizeButton.setUserData(2);
        fourLobbySizeButton.setUserData(4);
        eightLobbySizeButton.setUserData(8);
        fifteenDrawing.setUserData(15);
        thirtyDrawing.setUserData(30);
        minuteDrawing.setUserData(60);
        twoMinutesDrawing.setUserData(120);
        fifteenGuessing.setUserData(15);
        thirtyGuessing.setUserData(30);
        minuteGuessing.setUserData(60);
        twoMinutesGuessing.setUserData(120);
        languageChoiceBox.getItems().addAll("Russian","English");
        languageChoiceBox.setValue(settings.currentLanguage);
        switch (settings.lobbySize)
        {
            case(2)->{
                membersToggleGroup.selectToggle(twoLobbySizeButton);
            }
            case(4)->{
                membersToggleGroup.selectToggle(fourLobbySizeButton);
            }
            case(8)->{
                membersToggleGroup.selectToggle(eightLobbySizeButton);
            }
        }
        switch (settings.drawingTime)
        {
            case(15)-> {
                drawingToggleGroup.selectToggle(fifteenDrawing);
            }
            case(30)-> {
                drawingToggleGroup.selectToggle(thirtyDrawing);
            }
            case(60)->{
                drawingToggleGroup.selectToggle(minuteDrawing);
            }
            case(120)->{
                drawingToggleGroup.selectToggle(twoMinutesDrawing);
            }
        }
        switch (settings.guessingTime)
        {
            case(15)-> {
                guessingToggleGroup.selectToggle(fifteenGuessing);
            }
            case(30)-> {
                guessingToggleGroup.selectToggle(thirtyGuessing);
            }
            case(60)->{
                guessingToggleGroup.selectToggle(minuteGuessing);
            }
            case(120)->{
                guessingToggleGroup.selectToggle(twoMinutesGuessing);
            }
        }
        languageChoiceBox.setOnAction(event->{
            changeLanguage();
        });
        membersToggleGroup.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle != null) {
                settings.lobbySize = ((int) newToggle.getUserData());
            }
        });
        drawingToggleGroup.selectedToggleProperty().addListener((observable,oldToggle, newToggle)->{
            if(newToggle  != null)
            {
                settings.drawingTime = ((int) newToggle.getUserData());
            }
        });
        guessingToggleGroup.selectedToggleProperty().addListener((observable,oldToggle, newToggle)->{
            if(newToggle  != null)
            {
                settings.guessingTime = ((int) newToggle.getUserData());
            }
        });
    }
    private void changeLanguage(){
        settings.currentLanguage =(String) languageChoiceBox.getValue();
    }
    @FXML
    private void handleExit() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("lobbyPage.fxml"));
        Parent gameRoot = fxmlLoader.load();
        Scene scene = twoLobbySizeButton.getScene();
        scene.setRoot(gameRoot);
        GameApplication.currentController=  fxmlLoader.getController();
    }

}
