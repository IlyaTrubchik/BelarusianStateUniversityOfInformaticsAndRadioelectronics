

import com.sun.net.httpserver.HttpExchange;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Base64;
import java.util.HashMap;
import java.util.function.Consumer;

public class Controller {
    private static long MAX_CONTENT_LENGTH = 2097152;
    private static final int BUFFER_SIZE = 2097152;
    private static volatile boolean isPaused = false;

    private static String dir = Paths.get("").toAbsolutePath().toString() + "\\Storage\\";
    private static  String SERVER_URL ="http://localhost:8000/";
    @FXML
    Button enterButton;
    @FXML
    TextArea responseTA;
    @FXML
    TextField requestTF;
    @FXML
    ProgressBar downloadingPB;
    @FXML
    Button connectButton;
    @FXML
    TextField portTF;
    @FXML
    TextField ipTF;
    private HashMap<String, Consumer<String>> methodHandlers = new HashMap<>();
    @FXML
    private void loadURL(){
        SERVER_URL = "http://"+ipTF.getText()+":"+portTF.getText()+"/";
    }
    @FXML
    private void sendResponse(){
        String text = requestTF.getText();
        methodThread methodExecutor = new methodThread(text);
    }
    @FXML
    private void initialize(){
        methodHandlers.put("GET",this::handleGet);
        methodHandlers.put("PUT",this::handlePut);
        methodHandlers.put("POST",this::handlePost);
        methodHandlers.put("DELETE",this::handleDelete);
        methodHandlers.put("MOVE",this::handleMove);
        methodHandlers.put("COPY",this::handleCopy);
    }
    private long getFileLength(String path)
    {
        long result = 0;
        try {
            URL url = new URL(SERVER_URL + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            if (connection.getResponseCode() == 200) {
                result = connection.getContentLengthLong();
                System.out.println("File length:" + result);
            } else {
                System.out.println("Error!");
                return -1;
            }
        }
        catch (IOException e)
        {
            System.out.println("Error");
            return  -1;
        }
        return result;
    }
    private void handleGet(String command){
        String path = command.substring(command.indexOf("/")+1);
        RandomAccessFile file =  null;
        long fileSize = getFileLength(path);
        String fileName = "";
        if(path.contains("/")) {
            fileName = path.substring(path.lastIndexOf("/"));
        }else{
            fileName = path;
        }
        String saveDir  = dir+fileName;
        URL url =  null;
        try {
            url = new URL(SERVER_URL + path);
        }catch (IOException e)
        {
            System.out.println("Error");
        }
        if(fileSize<=MAX_CONTENT_LENGTH) {
            try {

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                if (connection.getResponseCode() == 200) {
                    byte[] content = connection.getInputStream().readAllBytes();
                    byte[] decoded = Base64.getDecoder().decode(content);
                    System.out.println(new String(decoded));
                } else {
                    System.out.println("Error, response code :" + connection.getResponseCode());
                }
            } catch (Exception e) {
                System.out.println("Cannot establish connection");
            }
        }else{
            try {
                String format = "#0.00";
                file = new RandomAccessFile(saveDir, "rw");
                long currentSize = 0;
                long startTime = System.currentTimeMillis();
                while (currentSize < fileSize) {
                    if(!isPaused) {
                        long pretime = System.currentTimeMillis();
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        String range = "bytes=" + currentSize + "-" + fileSize;
                        if (currentSize + BUFFER_SIZE - 1 < fileSize) {
                            range = "bytes=" + currentSize + "-" + (currentSize + BUFFER_SIZE - 1);
                        }
                        connection.setRequestProperty("Range", range);
                        if (connection.getResponseCode() == 206) {
                            byte[] content = connection.getInputStream().readAllBytes();
                            file.write(content, 0, content.length);
                            double speed =((content.length/((double)(System.currentTimeMillis()-pretime)/1000)/1024)/1024);
                            System.out.println("Speed:" + Math.round(speed)+"MByte/sec");
                            currentSize += content.length;
                            System.out.print("Bytes written:" + currentSize + "\r");
                            downloadingPB.setProgress((double) currentSize/fileSize);
                        }
                    }
                }
                file.close();
                System.out.println("Avarage speed:"+((fileSize/((double)(System.currentTimeMillis()-startTime)/1000)/1024)/1024));
                downloadingPB.setProgress(0);
            }catch (IOException e)
            {
                try {
                    file.close();
                } catch (IOException ex) {
                    System.out.println("Error closing file");
                }
                System.out.println("Error");
            }
        }
    }
    private void handlePut(String command){
        command  =  command.trim();
        String destinationPath = command.substring(command.indexOf("/")+1,command.indexOf(" ",command.indexOf("/")));
        String sourcePath = command.substring(command.indexOf(" ",4)+1);
        File file = new File(sourcePath);
        if(file.length()<=MAX_CONTENT_LENGTH) {
            byte[] content = null;
            try {
                content = Files.readAllBytes(Path.of(sourcePath));
            } catch (IOException e) {
                System.out.println("Error!");
            }
            try {
                URL url = new URL(SERVER_URL + destinationPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setDoOutput(true);
                OutputStream os = connection.getOutputStream();
                os.write(Base64.getEncoder().encode(content));
                os.flush();
                os.close();
                if (connection.getResponseCode() != 200) {
                    System.out.println("PUT request failed: " + connection.getResponseCode());
                } else {
                    System.out.println("ResponseCode:" + connection.getResponseCode() + ";Message:" + connection.getResponseMessage());
                }
            } catch (Exception e) {
                System.out.println("Cannot establish connection");
            }
        }else
        {
            try {
                RandomAccessFile rFile = new RandomAccessFile(file,"r");
                long offsite = 0;
                long endrange = BUFFER_SIZE;
                byte[] content = new byte[BUFFER_SIZE];
                rFile.seek(offsite);
                rFile.readFully(content);
                URL url = new URL(SERVER_URL + destinationPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
                connection.setDoOutput(true);
                OutputStream os = connection.getOutputStream();
                os.write(Base64.getEncoder().encode(content));
                os.flush();
                os.close();
                if (connection.getResponseCode() != 200) {
                    System.out.println("PUT request failed: " + connection.getResponseCode());
                } else {
                    System.out.println("ResponseCode:" + connection.getResponseCode() + ";Message:" + connection.getResponseMessage());
                }
                offsite = offsite+BUFFER_SIZE;
                while(offsite<file.length())
                {
                    if(!isPaused) {
                        long contentSize = BUFFER_SIZE;
                        if (file.length() - offsite < BUFFER_SIZE) {
                            contentSize = file.length() - offsite;
                        }
                        content = new byte[(int) contentSize];
                        rFile.seek(offsite);
                        rFile.readFully(content);
                        try {
                            url = new URL(SERVER_URL + destinationPath);
                            connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("POST");
                            connection.setDoOutput(true);
                            os  = connection.getOutputStream();
                            os.write(Base64.getEncoder().encode(content));
                            os.flush();
                            os.close();
                            if(connection.getResponseCode()==200){
                                //System.out.println("ResponseCode:"+connection.getResponseCode()+";Message:"+connection.getResponseMessage());
                                System.out.print("Sent:"+(offsite+contentSize)+"\r");
                                downloadingPB.setProgress((double)(offsite+contentSize)/file.length());
                            }
                            else {
                                System.out.println("Error, response code :"+connection.getResponseCode());
                            }
                        }
                        catch (Exception e)
                        {
                            System.out.println("Cannot establish connection on POST");
                        }
                        offsite = offsite+BUFFER_SIZE;
                    }
                    downloadingPB.setProgress(0);
                }
            } catch (Exception e) {
                System.out.println("Cannot establish connection on PUT");

                e.printStackTrace();
            }
        }
    }
    private void handlePost(String command){
        command  =  command.trim();
        command  =  command.trim();
        String destinationPath = command.substring(command.indexOf("/")+1,command.indexOf(" ",command.indexOf("/")));
        String sourcePath = command.substring(command.indexOf(" ",5)+1);
        byte[] content =  null;
        try {
            content = Files.readAllBytes(Path.of(sourcePath));
        } catch (IOException e) {
            System.out.println("Error!");
        }
        try {
            URL url = new URL(SERVER_URL + destinationPath);
            HttpURLConnection  connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStream os  = connection.getOutputStream();
            os.write(Base64.getEncoder().encode(content));
            os.flush();
            os.close();
            if(connection.getResponseCode()==200){
                System.out.println("ResponseCode:"+connection.getResponseCode()+";Message:"+connection.getResponseMessage());
            }
            else {
                System.out.println("Error, response code :"+connection.getResponseCode());
            }
        }
        catch (Exception e)
        {
            System.out.println("Cannot establish connection");
        }
    }
    private void handleDelete(String command){
        command = command.trim();
        String path = command.substring(command.indexOf("/")+1);
        try {
            URL url =new URL(SERVER_URL+path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("DELETE");
            if(connection.getResponseCode()==200)
            {
                System.out.println("Response code:200 DELETED");
            }
            else {
                System.out.println("Error, response code :"+connection.getResponseCode());
            }
        } catch (Exception e) {
            System.out.println("Cannot establish connection");
        }
    }
    private void handleMove(String command){
        command  =  command.trim();
        String sourcePath = command.substring(command.indexOf("/")+1, command.lastIndexOf(" "));
        String destinationPath = command.substring(command.lastIndexOf(" ")+2);
        handleCopy("COPY /"+ sourcePath + " /" + destinationPath);
        handleDelete("DELETE /" + sourcePath);
    }
    private void handleCopy(String command){
        command  =  command.trim();
        String sourcePath = command.substring(command.indexOf("/")+1, command.lastIndexOf(" "));
        String destinationPath = command.substring(command.lastIndexOf(" ")+2);
        try {
            URL url = new URL(SERVER_URL + sourcePath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            byte[] value;
            if (connection.getResponseCode() == 200) {
                value = connection.getInputStream().readAllBytes();
                URL putUrl = new URL(SERVER_URL + destinationPath);
                HttpURLConnection putConnection = (HttpURLConnection) putUrl.openConnection();
                putConnection.setRequestMethod("PUT");
                putConnection.setDoOutput(true);
                OutputStream outputStream = putConnection.getOutputStream();
                outputStream.write(value);
                outputStream.flush();
                outputStream.close();
                if (putConnection.getResponseCode() != 200) {
                    System.out.println("PUT request failed: " + connection.getResponseCode());
                } else {
                    System.out.println("ResponseCode:" + connection.getResponseCode() + ";Message:" + connection.getResponseMessage());
                }
            }
        }catch (Exception e)
        {
            System.out.println("Cannot establish connection");
        }
    }
    @FXML
    private void onPause(){
        if(isPaused){
            isPaused = false;
        }
        else
        {
            isPaused = true;
        }
    }
    private class methodThread extends  Thread{
        String text = "";

        methodThread(String text){
            this.text = text;
            this.start();
        }
        public void run(){
            String method = text.trim().substring(0,text.indexOf(" ")).trim();
            Consumer<String> methodHandler = methodHandlers.get(method);
            if(methodHandler != null)
            {
                methodHandler.accept(text);
            }
            this.interrupt();
        }
    }
}