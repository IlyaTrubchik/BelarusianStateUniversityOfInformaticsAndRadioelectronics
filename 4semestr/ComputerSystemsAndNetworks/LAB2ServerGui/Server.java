import javafx.application.Platform;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;




public class Server extends Thread {

    public  static  final ArrayList<clientThread> clientThreads = new ArrayList<>();
    private static int PORT = 9999;//по умолчанию

    private static String name = "UNKNOWN";
    private static ServerSocket serverSocket;
    public  static String IP = "localhost";// по умолчанию

    public void run()
    {
        try {
            serverSocket = new ServerSocket(PORT);
            InetAddress ip = InetAddress.getLocalHost();
            Server.IP= getIpString(ip.getAddress());
            Platform.runLater(()->ServerApplication.controller.updateIP());
            ServerApplication.controller.update("Server started succesfully");
        }
        catch (IOException e)
        {
            ServerApplication.controller.update("Can't start server on this port");
        }
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientThreads.add(new clientThread(clientSocket,name));
            }
        }
        catch (IOException e)
        {
            if(!serverSocket.isClosed()) {
                ServerApplication.controller.update("Can't connect with client.Disabling Server");
                this.stopServer();
            }

        }
    }
    public void changePort(int port){
        PORT = port;
    }

    public void stopServer(){
        try {
            for(clientThread client:clientThreads)
            {
                try {
                    client.out.close();
                    client.in.close();
                    client.clientSocket.close();
                    client.interrupt();
                }
                catch(IOException e)
                    {
                        ServerApplication.controller.update("Client thread already closed");
                    }
            }
            serverSocket.close();
        }
        catch(IOException e)
        {
            ServerApplication.controller.update("Server already disabled");
        }
    }
    public static String getIpString(byte[] ip)
    {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<4;i++)
        {
            sb.append(Byte.toUnsignedInt(ip[i])+".");
        }
        sb.deleteCharAt(sb.length()-1);
        return  sb.toString();
    }
}
class clientThread extends Thread {
    Socket clientSocket;
    String Name = "UNKNOWN";
    public BufferedWriter out;
    public BufferedReader in;
    public clientThread(Socket clientSocket, String name)
    {
        this.clientSocket =clientSocket;
        this.Name = name;
        try {
            this.out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            start();
        }
        catch (IOException e)
        {

            ServerApplication.controller.update("Client disconnected!");
        }

    }
    public void run(){
        String line = "";
        try {
            while (true) {
                line = this.in.readLine();

                if(line!= null && line.startsWith("/n"))
                {
                    this.Name = line.substring(line.indexOf("/n")+2,line.length());
                }
                else if(line!=null){
                    System.out.println(line);
                    ServerApplication.controller.update(line);
                    ArrayList<Integer> deadClients =new ArrayList<>();
                    for (clientThread client : Server.clientThreads) {
                        try {
                            client.out.write(this.Name + ":" + line + "\n");
                            client.out.flush();
                        }
                        catch(IOException e){
                            deadClients.add(Server.clientThreads.indexOf(client));
                            ServerApplication.controller.update("Client disconnected");
                        }
                    }
                    for(int i = 0;i<deadClients.size();i++)
                    {
                        clientThread client = Server.clientThreads.get(deadClients.get(i));
                        client.in.close();
                        client.out.close();
                        client.clientSocket.close();
                        client.interrupt();
                        Server.clientThreads.remove(client);
                    }
                }
            }
        }
        catch (IOException e)
        {
          // ServerApplication.controller.showErrorAlert("IOException occurs");
           // ServerApplication.controller.update("Client disconnected");
        }
    }

}