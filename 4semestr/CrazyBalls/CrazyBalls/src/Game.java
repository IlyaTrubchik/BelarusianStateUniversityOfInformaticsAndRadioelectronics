
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class Game extends Application{
    public static final int DEFAULT_HEIGHT = 720;
    public static final int DEFAULT_WIGTH = 1280;

    private static final double AREA_COEFFICIENT = 0.02;
    private GraphicsContext gc;
    Players SessionPlayers = new Players();
    GameArea gameField;
    Menu menu;
    boolean isPaused = false;
    SettingsWindow settingsWindow;

    private void update(){
        this.gameField.update();
    };

    private void render(){
        this.gameField.render();
    };
    private void checkCollision(){
        this.gameField.checkCollisions();
    }

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        Canvas canvas = new Canvas(DEFAULT_WIGTH,DEFAULT_HEIGHT);
        gc = canvas.getGraphicsContext2D();
        root.setCenter(canvas);
        this.Initialize();
        AnimationTimer gameLoop=  new AnimationTimer() {
            @Override
            public void handle(long l) {
                checkCollision();
                update();
                render();
            }
        };
        gameLoop.start();
        stage.setScene(new Scene(root, DEFAULT_WIGTH, DEFAULT_HEIGHT));
        stage.getScene().setOnKeyPressed(event -> {
            if(event.getCode()==KeyCode.ESCAPE)
            {
                if(!isPaused)
                {
                    gameLoop.stop();
                    this.menu.render();
                    isPaused = true;
                }else {
                    isPaused = false;
                    render();
                    gameLoop.start();
                }
            }else this.gameField.platform.eventHandler(event);
        });
        stage.getScene().setOnKeyReleased(event->{
            if(!isPaused && event.getCode()==KeyCode.LEFT || event.getCode()==KeyCode.RIGHT)
            {
                this.gameField.platform.setDirection(0);
            }
            else if(isPaused && event.getCode()==KeyCode.S)
            {
                try {
                    Serializer.Save(this.gameField);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        stage.show();
    }
    public static void main(String[] args)
    {
        launch();
    }

    private void Initialize(){
        this.menu = new Menu(DEFAULT_WIGTH*0.25,DEFAULT_HEIGHT*0.1,DEFAULT_WIGTH*0.75,DEFAULT_HEIGHT*0.9,Color.BLACK,gc);
        this.gameField = new GameArea(AREA_COEFFICIENT/2*DEFAULT_WIGTH,
                AREA_COEFFICIENT/2*DEFAULT_HEIGHT, DEFAULT_WIGTH-AREA_COEFFICIENT/2*DEFAULT_WIGTH,
                DEFAULT_HEIGHT-AREA_COEFFICIENT/2*DEFAULT_HEIGHT,Color.WHITE,gc);
        this.gameField.initialize();
    }
}
