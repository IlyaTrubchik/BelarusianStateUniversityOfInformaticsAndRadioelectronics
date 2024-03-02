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
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

public class DrawingPageController {
    @FXML
    Canvas drawingArea;
    Color currentBrushColor;

    private int currentBrushSize = 10;

    @FXML
    ColorPicker colorPicker;

    @FXML
    Text drawingWord;
    @FXML
    Text timerText;
    @FXML
    Slider brushSizeSlider;
    private double startX;
    private double startY;

    @FXML
    private void handleBrushDrag(javafx.scene.input.MouseEvent event) {
        GraphicsContext gc = drawingArea.getGraphicsContext2D();
        gc.setStroke(currentBrushColor);
        gc.setLineWidth(currentBrushSize);
        gc.strokeLine(startX, startY, event.getX(), event.getY());
        startX = event.getX();
        startY = event.getY();
    }

    @FXML
    private void handleBrushPress(javafx.scene.input.MouseEvent event) {
        startX = event.getX();
        startY = event.getY();
    }

    @FXML
    private void changeColor(ActionEvent event) {
        currentBrushColor = colorPicker.getValue();
    }

    @FXML
    private void changeBrushSize(){
        currentBrushSize = (int) brushSizeSlider.getValue();
    }
    public void setDrawingWord(String word) {
        if(LobbySettingsController.settings.currentLanguage.equals("English")) {
            drawingWord.setText(GameApplication.toEnglish.get(word));
        }
        else{
            drawingWord.setText(word);
        }
    }

    public void startTimer() {
        Timeline timeline = new Timeline();
        final int[] i = {LobbySettingsController.settings.drawingTime};
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
            timerText.setText("Time left:" + i[0] + " secs");
            i[0]--;
        });
        timeline.getKeyFrames().add(keyFrame);
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
                drawingArea.setDisable(true);
                if (GameApplication.player != null) {
                    try {
                        GameApplication.player.sendMessage("/image");
                        GameApplication.player.setImage(canvasToByteArray());
                        GameApplication.player.sendPlayerImage(GameApplication.player.playerInfo.getPlayerImage());
                        GameApplication.player.sendMessage("READY");
                    } catch (IOException e) {
                        System.out.println("error!");
                    }
                } else {
                    Server.readyCounter++;
                    GameApplication.hostPlayer.setPlayerImage(canvasToByteArray());
                    GameApplication.server.addImage(new imageContainer(GameApplication.hostPlayer.getPlayerImage(),
                            GameApplication.hostPlayer.getName(),GameApplication.hostPlayer.getDrawingWord()));
                    Timer timer = new Timer();

                    timer.scheduleAtFixedRate(new TimerTask() {
                        int count = 0;
                        @Override
                        public void run() {
                            if (Server.readyCounter == Server.playersList.size() + 1) {
                                System.out.println("Server will start guessing");
                                try {
                                    GameApplication.server.startGuessing();
                                }
                                catch (IOException e)
                                {
                                    System.out.println("Cant start Guessing");
                                }
                                timer.cancel();
                            }
                        }
                         }, 0, 500);
                    }
                }
        });
        timeline.setCycleCount(LobbySettingsController.settings.drawingTime+1);
        timeline.play();
    }
    public void changePage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("guessingPage.fxml"));
        Parent root =fxmlLoader.load();
        Scene scene = colorPicker.getScene();
        GameApplication.currentController = fxmlLoader.getController();
        scene.setRoot(root);
    }
    public byte[] canvasToByteArray(){
        WritableImage image = drawingArea.snapshot(null, null);
        BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
        } catch (IOException e) {
            System.out.println("error");
        }
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return byteArray;
    }
}
