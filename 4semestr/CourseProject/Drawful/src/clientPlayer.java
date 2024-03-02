import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.*;
import java.net.Socket;

public class clientPlayer extends  Thread {

    PlayerInfo  playerInfo =  new PlayerInfo();
    public Socket socket;
    BufferedWriter out;
    BufferedReader in;
    boolean isGuessing = false;
    boolean isImage=false;
    byte[] currentImage;
    private String guessingWord = "DEFAULT";
    public clientPlayer(Socket playerSocket,String name)
    {
        try {
            this.socket = playerSocket;
            in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
            playerInfo.setPlayerName(name);
            this.start();
        }
        catch(IOException e)
        {
            System.out.println("Socket error occurs!!");
        }
    }
    public void disable(){
        try {
            this.out.write("/d");
            this.out.write("\n");
            this.out.flush();
            this.out.close();
            this.in.close();
            this.socket.close();
            this.interrupt();
        }
        catch (IOException e)
        {
            System.out.println("Already closed");
        }
    }
    public void run(){
        while(true)
        {
            try {
                if(!isImage) {
                    String line = in.readLine();
                    handleMessage(line);
                }else
                {
                    DataInputStream dataIn = new DataInputStream(socket.getInputStream());
                    out.write("/sendImage");
                    out.write("\n");
                    out.flush();
                    //String line = in.readLine();
                    //int number = Integer.parseInt(line.substring(line.indexOf(" ")+1));
                    //int length=number;
                    int length = dataIn.readInt();
                    byte[] data = new byte[length];
                    dataIn.readFully(data,0,length);
                    this.currentImage = data;
                    ((GuessingPageController)GameApplication.currentController).updateCanvas(this.currentImage);
                    isImage = false;
                    out.write("/recieve");
                    out.write("\n");
                    out.flush();
                }
            } catch (IOException e) {
                System.out.println("Error!Client");
                disable();
                break;
            }
        }
    }
    public void setImage(byte[] Image){
        this.playerInfo.setPlayerImage(Image);
    }
    public void sendPlayerImage(byte[] image)
    {
        try {
            DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
            dataOut.writeInt(image.length);
            dataOut.write(image);
            dataOut.flush();
        } catch (IOException e) {
            System.out.println("Error!");
        }
    }
    public void sendMessage(String message) throws IOException {
        out.write(message);
        out.write("\n");
        out.flush();
    }
    public void handleMessage(String message) throws IOException {
        if(message.equals("/start"))
        {
            ((WaitingPageController) GameApplication.currentController).playersChangeScreen();
            ((DrawingPageController)GameApplication.currentController).setDrawingWord(this.playerInfo.getDrawingWord());
            ((DrawingPageController)GameApplication.currentController).startTimer();
        }
        else if(message.equals("/guess"))
        {
            ((GuessingPageController)GameApplication.currentController).startTimer();
        }
        else if(message.startsWith("score"))
        {
            ((GuessingPageController)GameApplication.currentController).scoreTable.setText("Score:" +
                    message.substring(message.indexOf(":")+1));
        }
        else if(message.startsWith("Drawing")){
            this.playerInfo.setDrawingWord(message.substring(message.indexOf(":")+1));
        }
        else if(message.startsWith("/image"))
        {
           isImage = true;
         //  this.sendMessage("/sendImage");
        }
        else if(message.equals("/next"))
        {
            ((DrawingPageController)GameApplication.currentController).changePage();
        }
        else if(message.equals("/endGame"))
        {
            ((GuessingPageController) GameApplication.currentController).endGamePage();

        }
        else if(message.startsWith("/scoreT"))
        {
            String name = message.substring(message.indexOf("(")+1,message.indexOf(")"));
            String score = message.substring(message.indexOf("[")+1,message.indexOf("]"));
            PlayerInfo player = new PlayerInfo();
            player.setPlayerName(name);
            player.setScore(Integer.parseInt(score));
            GameApplication.scoreMap.add(player);
        }
        else if(message.startsWith("/dtime"))
        {
            int time = Integer.parseInt(message.substring(message.indexOf(":")+1));
            LobbySettingsController.settings.drawingTime = time;
        }
        else if(message.startsWith("/gtime"))
        {
            int time = Integer.parseInt(message.substring(message.indexOf(":")+1));
            LobbySettingsController.settings.guessingTime = time;
        }else if (message.startsWith("/gword"))
        {
            String word = message.substring(message.indexOf("d")+1);
            this.guessingWord = word;
        }
        else if (message.equals("/d")){
            FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("menu.fxml"));
            Parent root =fxmlLoader.load();
            Scene scene = GameApplication.mainScene;
            GameApplication.currentController = fxmlLoader.getController();
            scene.setRoot(root);
        }
    }
    public String getGuessingWord(){
        return this.guessingWord;
    }
}
