import java.io.*;
import java.net.Socket;


public class Client {
    public static BufferedReader in;
    public static BufferedWriter out;
    public static String Line = "";
    public static Socket socket;
    private static String IP = "localhost";//по умолчанию
    private static int PORT = 9999;//по умолчанию

    public static boolean isConnected =false;

    private inThread readThread;
    public void connect()
    {
        if(isConnected == false) {
            try {
                socket = new Socket(IP, PORT);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                isConnected =true;
                readThread = new inThread();
                readThread.start();
                ClientApplication.controller.update("Connect successful!");
            } catch (IOException e) {
              ClientApplication.controller.update("Can't connect to this server");
            }
        }
        else {
           ClientApplication.controller.update("You already connected");
        }
    }
    public void changePort(int Port)
    {
        PORT = Port;
    }
    public void changeIP(String ip)
    {
        IP = ip;
    }

    public void send(String line){
        try {
            System.out.println(line);
            out.write(line + "\n");
            out.flush();
        }
        catch(IOException e)
        {
            disconnect();
           ClientApplication.controller.update("Connect to server!");
        }
    }
    public  void disconnect(){
        try {
            readThread.interrupt();
            socket.close();
            out.close();
            in.close();
            isConnected =false;
        }
        catch (IOException e)
        {
          ClientApplication.controller.update("Socket close error");
        }
    }
}

class inThread extends Thread{
    public void run(){
        String line  = "";
        try{
            while(true)
            {
                line = Client.in.readLine();
                ClientApplication.controller.update(line);
               // System.out.println(line);
            }
        }
        catch (IOException e)
        {
            ClientApplication.controller.update("Server disabled");
            this.interrupt();
        }
    }
}
