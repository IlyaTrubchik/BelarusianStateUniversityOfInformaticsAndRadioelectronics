import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class GuessingPageController {
    @FXML
    Canvas pictureArea;
    @FXML
    TextField answerTextField;
    @FXML
    Button sendAnswerButton;
    @FXML
    Text timerText;
    @FXML
    Text scoreTable;
    @FXML
    Text guessingWord;
    @FXML
    AnchorPane guessedWordPane;

    @FXML
    private void sendAnswer() throws IOException {
        if(GameApplication.player!=null)
        {
            try {
                GameApplication.player.sendMessage(answerTextField.getText());
               // sendAnswerButton.setDisable(true);
            } catch (IOException e) {
                System.out.println("Error");
            }
        }else
        {
               GameApplication.hostPlayer.answer = answerTextField.getText();
               //GameApplication.server.answered = GameApplication.server.answered+1;
               //sendAnswerButton.setDisable(true);
               Server.readyCounter++;
              // startTimer();
        }
    }
    public void startTimer() {
        Timeline timeline = new Timeline();
        final int[] i = {LobbySettingsController.settings.guessingTime};
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            //timerText.setText("Time left:" + i[0] + " secs");
            i[0]--;
            timerText.setText("Time left:" + i[0] + " secs");
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.setCycleCount(LobbySettingsController.settings.guessingTime+1);
        timeline.setOnFinished(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    nextStep();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            private void nextStep() throws IOException {
                if(GameApplication.player!=null)
                {
                    if(LobbySettingsController.settings.currentLanguage.equals("English")) {
                        showGuessingWord(GameApplication.toEnglish.get(GameApplication.player.getGuessingWord()));
                    }else
                    {
                        showGuessingWord(GameApplication.player.getGuessingWord());
                    }
                }else
                {
                    if(LobbySettingsController.settings.currentLanguage.equals("English"))
                    {
                        showGuessingWord(GameApplication.toEnglish.get(Server.images.get(Server.currGuessingImg).imageMeaning));
                    }
                    else
                    {
                        showGuessingWord(Server.images.get(Server.currGuessingImg).imageMeaning);
                    }

                }
                Timeline timeline = new Timeline();
                KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
                });
                timeline.getKeyFrames().add(keyFrame);
                timeline.setCycleCount(5);
                timeline.setOnFinished(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        try {
                            hideGuessingWord();
                            if (GameApplication.player != null) {
                                try {
                                    GameApplication.player.sendMessage("READY");
                                } catch (IOException e) {
                                    System.out.println("error!");
                                }
                            } else {
                                GameApplication.server.checkAnswers(Server.currGuessingImg);
                                Server.readyCounter=0;
                                Server.currGuessingImg++;
                                System.out.println("i'm here");
                                if(Server.currGuessingImg<=Server.playersList.size())
                                {
                                    GameApplication.server.sendGuessImage(Server.currGuessingImg);
                                }
                                else{
                                    GameApplication.server.sendScoreTable();
                                    GameApplication.server.endGame();
                                    endGamePage();
                                }
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
                timeline.play();
            }
        });
        timeline.play();
    }

    public void updateCanvas(byte[] imageArr)
    {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageArr);
        try {
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            WritableImage image = SwingFXUtils.toFXImage(bufferedImage, null);
            GraphicsContext gc = pictureArea.getGraphicsContext2D();
            gc.drawImage(image, 0, 0);
        } catch (IOException e) {

        }
    }
    private void showGuessingWord(String word){
        this.guessedWordPane.setVisible(true);
        this.guessingWord.setText(word);
    }
    private void hideGuessingWord()
    {
        this.guessedWordPane.setVisible(false);
    }
    public void endGamePage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("endGamePage.fxml"));
        Parent root =fxmlLoader.load();
        Scene scene = sendAnswerButton.getScene();
        GameApplication.currentController = fxmlLoader.getController();
        scene.setRoot(root);
        ((endGameController) GameApplication.currentController).initializeScoreTable();
    }

}
