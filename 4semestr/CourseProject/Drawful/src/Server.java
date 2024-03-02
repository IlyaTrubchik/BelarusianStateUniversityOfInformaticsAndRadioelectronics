import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Server extends Thread {
    protected static ArrayList<serverPlayer> playersList;

    public static ArrayList<imageContainer> images = new ArrayList<>();

    private  int PORT = 4000;
    public static int readyCounter = 0;
    private int lobbySize = 2;
    private boolean isAllReady = false;

    private  int guessingTime = 15;
    private  int drawingTime = 15;
    public static int currGuessingImg = 0;
   // public int answered = 0;


    private static ArrayList<String> playersWords;
    //public static String serverToDo = "";
    static ServerSocket serverSocket;

    public void run(){
        startServer();
    }

    public Server (int port)
    {
        this.PORT = port;
    }
    public void startServer(){
        try {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ((LobbyController) GameApplication.currentController).setHostWaitingPage();
                }
            });
            lobbySize = LobbySettingsController.settings.lobbySize;
            drawingTime = LobbySettingsController.settings.drawingTime;
            guessingTime = LobbySettingsController.settings.guessingTime;
            serverSocket = new ServerSocket(this.PORT);
            playersList = new ArrayList<>();
            //После цикла должна стать доступна кнопка startGame
            int i = 0;
            while (i < lobbySize - 1) {
                Socket playerSocket = serverSocket.accept();
                serverPlayer player = new serverPlayer(playerSocket);
                sendMessage(player, "/gtime:" + guessingTime);
                sendMessage(player, "/dtime:" + drawingTime);
                playersList.add(player);
                i++;
            }
            ((WaitingPageController) GameApplication.currentController).setStartable();
            System.out.println("All players connected to lobby");
        }
        catch (IOException e)
        {
            try {
                System.out.println("cant start server on this port");
                FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("menu.fxml"));
                Parent root = fxmlLoader.load();
                Scene scene = GameApplication.mainScene;
                GameApplication.currentController = fxmlLoader.getController();
                scene.setRoot(root);
            }
            catch (IOException exc)
            {
                System.out.println("Cant load prev page");
            }
        }
    }
    public void disable(){
        for(serverPlayer player:playersList)
        {
            try {
                player.out.write("/d");
                player.out.write("\n");
                player.out.flush();
                player.out.close();
                player.in.close();
                player.socket.close();
                player.interrupt();
            }
            catch (IOException e)
            {
                System.out.println("Socket already closed");
            }
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Server socket already closed");
        }
    }
    public void startGame()
    {
            getWords();
            sendWordsAll();
            sendMessageAll("/start");
    }

    private void getWords(){
        playersWords = new ArrayList<>();
        Random rand = new Random();
        while(playersWords.size()<lobbySize) {
            playersWords.add(GameApplication.vocabulary.get(rand.nextInt(GameApplication.vocabulary.size())));
        }
    }

    private  void sendWordsAll(){
        for(int i = 1;i<playersWords.size();i++)
        {
            StringBuilder  sb = new StringBuilder();
            sb.append("Drawing Word :");
            sb.append(playersWords.get(i));
            sendMessage(playersList.get(i-1),sb.toString());
            playersList.get(i-1).playerInfo.setDrawingWord(playersWords.get(i));
        }
        GameApplication.hostPlayer.setDrawingWord(playersWords.get(0));
    }
    private  void sendMessage(serverPlayer player, String message){
        try {
            player.out.write(message);
            player.out.write("\n");
            player.out.flush();
        }
        catch(IOException e)
        {
            System.out.println("Can't send message to player");
        }
    }
    private  void sendMessageAll(String message){
        for (serverPlayer player :playersList) {
                sendMessage(player,message);
        }
        if(message.equals("/start"))
        {
            ((DrawingPageController)GameApplication.currentController).setDrawingWord
                    (GameApplication.hostPlayer.getDrawingWord());
            ((DrawingPageController)GameApplication.currentController).startTimer();
        }
    }
    public void endGame()
    {
        sendMessageAll("/endGame");
    }
    public void sendScoreTable(){
        sendMessageAll("/scoreT name:("+GameApplication.hostPlayer.getName()+")"+"score:["+GameApplication.hostPlayer.getScore()+"]");
        GameApplication.scoreMap.add(GameApplication.hostPlayer);
        for (serverPlayer player:playersList) {
            sendMessageAll("/scoreT name:("+player.playerInfo.getName()+")"+"score:["+player.playerInfo.getScore()+"]");
            GameApplication.scoreMap.add(player.playerInfo);
        }
    }
    public void updatePlayersScore()
    {
        for (serverPlayer player:playersList) {
            String message = "score:"+player.playerInfo.getScore();
            sendMessage(player,message);
        }
    }
    public void addImage(imageContainer image){
        images.add(image);
    }
    public void startGuessing() throws IOException {
        readyCounter = 0;
        sendMessageAll("/next");
        ((DrawingPageController) GameApplication.currentController).changePage();
        System.out.println("pages changed");
        sendGuessImage(0);
    }
    public void sendGuessImage(int i){
        String answer = "";
        System.out.println("Guessing images");
        sendMessageAll("/image");
        //sendImageAll(images.get(i).image);
        ((GuessingPageController) GameApplication.currentController).updateCanvas(images.get(i).image);
        for (serverPlayer player : playersList) {
            player.isGuessing = true;
        }
        //sendMessageAll("/guess");
        System.out.println("Check - set false");
        ((GuessingPageController) GameApplication.currentController).startTimer();
    }
    public void checkAnswers(int i) throws IOException {
         //   System.out.println(answer);
            for (serverPlayer player : playersList) {
                if ((player.playerInfo.answer.equals(images.get(i).imageMeaning) ||
                        player.playerInfo.answer.equals(GameApplication.toEnglish.get(images.get(i).imageMeaning))) &&
                        !player.playerInfo.getName().equals(images.get(i).playerName))
                    player.playerInfo.incScore(1);
            }
            if (GameApplication.hostPlayer.answer.equals(images.get(i).imageMeaning) &&
                    !GameApplication.hostPlayer.getName().equals(images.get(i).playerName))
                GameApplication.hostPlayer.incScore(1);
          //  GameApplication.server.answered = 0;
            updatePlayersScore();
        ((GuessingPageController) GameApplication.currentController).scoreTable.setText("Score:"+GameApplication.hostPlayer.getScore());
    }
     public void checkConnectedPlayers(){
        ArrayList<Integer> discPlayers = new ArrayList<>();
        for (serverPlayer player:playersList) {
            try {
                player.socket.close();
            }
            catch (IOException e)
            {
                discPlayers.add(playersList.indexOf(player));
                player.disable();
            }
        }
        for (Integer index:discPlayers) {
            playersList.remove(index);
        }
    }


}