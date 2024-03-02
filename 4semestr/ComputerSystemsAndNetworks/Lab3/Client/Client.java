import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Scanner;


public class Client {
    private static  String SERVER_URL ="http://localhost:8000/";
    public static void main(String[] args) throws IOException {
        System.out.println("Enter ip:");
        Scanner scanner = new Scanner(System.in);
        String ip = scanner.nextLine();
        System.out.println("Enter port");
        String port  =  scanner.nextLine();
        SERVER_URL = "http://"+ip+":"+port+"/";
        while(true) {
            System.out.println("Write command:");
            String line  = scanner.nextLine();
            if (line.startsWith("GET")) {
                line  =  line.trim();
                String path = line.substring(line.indexOf("/")+1);
                get(path);
            } else if (line.startsWith("PUT")) {
                line  =  line.trim();
                String destinationPath = line.substring(line.indexOf("/")+1,line.indexOf(" ",line.indexOf("/")));
                String sourcePath = line.substring(line.indexOf(" ",4)+1);
                byte[] content = Files.readAllBytes(Path.of(sourcePath));
                put(destinationPath, content);
            } else if (line.startsWith("POST")) {
                line  =  line.trim();
                String destinationPath = line.substring(line.indexOf("/")+1,line.indexOf(" ",line.indexOf("/")));
                String sourcePath = line.substring(line.indexOf(" ",5)+1);
                byte[] content = Files.readAllBytes(Path.of(sourcePath));
                post(destinationPath, content);
            } else if (line.startsWith("DELETE")) {
                line  =  line.trim();
                String path = line.substring(line.indexOf("/")+1);
                delete(path);
            } else if (line.startsWith("COPY")) {
                line  =  line.trim();
                String sourcePath = line.substring(line.indexOf("/")+1, line.lastIndexOf(" "));
                String destinationPath = line.substring(line.lastIndexOf(" ")+2);
                copy(sourcePath, destinationPath);
            } else if (line.startsWith("MOVE")) {
                line  =  line.trim();
                String sourcePath = line.substring(line.indexOf("/")+1, line.lastIndexOf(" "));
                String destinationPath = line.substring(line.lastIndexOf(" ")+2);
                move(sourcePath, destinationPath);
            }
        }
    }
    private static void get(String path)
    {
        try {
            URL url =new URL(SERVER_URL+path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            if(connection.getResponseCode()==200)
            {
                byte[] content = connection.getInputStream().readAllBytes();
                byte[] decoded = Base64.getDecoder().decode(content);
                System.out.println(new String(decoded));
            }else{
                System.out.println("Error, response code :"+connection.getResponseCode());
            }
        } catch (Exception e) {
            System.out.println("Cannot establish connection");
        }
    }
    private static void delete(String path)
    {
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

    private static void put(String path,byte[] value)
    {
        try {
            URL url = new URL(SERVER_URL + path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setDoOutput(true);
            OutputStream os = connection.getOutputStream();
            os.write(Base64.getEncoder().encode(value));
            os.flush();
            os.close();
            if (connection.getResponseCode() != 200) {
                System.out.println("PUT request failed: " + connection.getResponseCode());
            }
            else{
                System.out.println("ResponseCode:"+connection.getResponseCode()+";Message:"+connection.getResponseMessage());
            }
        }
        catch (Exception e) {
            System.out.println("Cannot establish connection");
        }
    }
    private  static  void post(String path,byte[] content)
    {
        try {
            URL url = new URL(SERVER_URL + path);
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
    private static void copy(String sourcePath, String destinationPath)
    {
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
    private static void move(String sourcePath,String destinationPath)
    {
        copy(sourcePath,destinationPath);
        delete(sourcePath);
    }
}
