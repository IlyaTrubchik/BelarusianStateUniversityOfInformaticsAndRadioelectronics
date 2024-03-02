import java.io.*;
import java.net.Socket;

public class serverPlayer extends  Thread {
    PlayerInfo playerInfo = new PlayerInfo();
    public Socket socket;
    boolean isGuessing = false;
    BufferedWriter out;
    BufferedReader in;
    boolean isImage=false;
    boolean recieved = false;
    public serverPlayer(Socket playerSocket)
    {
        try {
            this.socket = playerSocket;
            in = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
            this.start();
        }
        catch(IOException e)
        {
            System.out.println("Socket error occurs!!");
        }
    }
    public void sendImage(byte[] image,Socket socket)
    {
        try {
            System.out.println("Start sending image");
            DataOutputStream dataOut = new DataOutputStream(socket.getOutputStream());
            dataOut.writeInt(image.length);
            dataOut.write(image);
            dataOut.flush();
            System.out.println("ImageSended");
        } catch (IOException e) {
            System.out.println("Error!");
        }
    }
    public void disable(){
        try {
            this.out.close();
            this.in.close();
            this.socket.close();
            this.interrupt();
        }
        catch (IOException e)
        {
            System.out.println("Socket already closed");
        }
    }
    public void run(){
        while(true)
        {
            try {
                if(!isImage) {
                    String line = in.readLine();
                    if(isGuessing && line!=null)
                    {
                        if (line.equals("READY")) {
                            Server.readyCounter++;
                            System.out.println("CounterUpdated");
                        }
                        else if (line.startsWith("/name"))
                        {
                            playerInfo.setPlayerName(line.substring(line.indexOf("e")+1));
                        }
                        else if(line.equals("/image"))
                        {
                            isImage = true;
                        }
                        else if(line.equals("/recieve"))
                        {
                            recieved =true;
                            this.out.write("/guess");
                            this.out.write("\n");
                            this.out.flush();
                            this.out.write("/gword"+Server.images.get(Server.currGuessingImg).imageMeaning);
                            this.out.write("\n");
                            this.out.flush();
                        }
                        else if (line.equals("/sendImage"))
                        {
                            sendImage(Server.images.get(Server.currGuessingImg).image,this.socket);
                        }
                        else if(line.equals("/d"))
                        {
                            Server.playersList.remove(Server.playersList.indexOf(this));
                            disable();

                            break;
                        }
                        else
                        {
                            this.playerInfo.answer = line;
                        }
                    }else
                    {
                        if(line!=null) {
                            System.out.println(line);
                            if (line.equals("READY")) {
                                Server.readyCounter++;
                                System.out.println("CounterUpdated");
                            } else if (line.equals("/image")) {
                                isImage = true;
                            } else if (line.startsWith("/name")) {
                                playerInfo.setPlayerName(line.substring(line.indexOf("e") + 1));
                            } else if (line.equals("/recieve")) {
                                recieved = true;
                                this.out.write("/guess");
                                this.out.write("\n");
                                this.out.flush();
                                this.out.write("/gword" + Server.images.get(Server.currGuessingImg).imageMeaning);
                                this.out.write("\n");
                                this.out.flush();
                            } else if (line.equals("/sendImage")) {
                                sendImage(Server.images.get(Server.currGuessingImg).image, this.socket);
                            }
                            else if(line.equals("/d"))
                            {
                                Server.playersList.remove(Server.playersList.indexOf(this));
                                disable();
                                break;
                            }
                        }
                    }
                }
                else
                {
                    DataInputStream dataIn = new DataInputStream(socket.getInputStream());
                    int length =  dataIn.readInt();
                    byte[] data = new byte[length];
                    dataIn.readFully(data,0,length);
                    this.playerInfo.setPlayerImage(data);
                    GameApplication.server.addImage(new imageContainer(this.playerInfo.getPlayerImage(),
                            this.playerInfo.getName(),this.playerInfo.getDrawingWord()));
                    isImage = false;
                }
            } catch (IOException e) {
                System.out.println("Error!SErver");
                disable();
                Server.playersList.remove(Server.playersList.indexOf(this));
                break;
            }
        }
    }

}
