import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameApplication extends Application {
    public static Object currentController;
    public static clientPlayer player;
    public static PlayerInfo hostPlayer = new PlayerInfo();
    public static  Server server;
    public static Scene mainScene;

    public static MessageBox messageBox;
    public static Map<String,String> toEnglish = new HashMap<>();
    public static  ArrayList<String> vocabulary;

    public static ArrayList<PlayerInfo> scoreMap = new ArrayList<>();
    public static boolean isStarted=false;
    @Override
    public void start(Stage stage) throws IOException {

            FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("menu.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 506);
            mainScene = scene;
            loadWords();
            currentController = fxmlLoader.getController();
            stage.setTitle("Main");
            //stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
            ((MenuController) currentController).updateName("DEFAULT");
            messageBox = new MessageBox(0,0,(int)scene.getWidth(),(int)scene.getHeight(),"DEfault");
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    if(isStarted) {
                        if (GameApplication.player != null) {
                            GameApplication.player.disable();
                        } else {
                            GameApplication.server.disable();
                        }
                    }
                }
            });
    }
    private void loadWords(){
        try {
            File file = new File("E:\\KSIS\\KursProject(Drawful)\\DrawFul_Server\\src\\engwords.txt");
            File ruFile =  new File("E:\\KSIS\\KursProject(Drawful)\\DrawFul_Server\\src\\russianwords.txt");
            BufferedReader bufIn = new BufferedReader(new FileReader(file));
            BufferedReader ruIn =  new BufferedReader(new FileReader(ruFile));
            String line;
            String ruLine;
            vocabulary =new ArrayList<>();
            while((line=bufIn.readLine())!=null)
            {
                ruLine =  ruIn.readLine();
                vocabulary.add(ruLine);
                this.toEnglish.put(ruLine,line);
            }
        }
        catch(IOException e)
        {
            System.out.println("can't load vocabulary");
        }
    }

    public static void main(String[] args) {
        launch();
    }
}