package colorfight;

/**
 * @author Nisarg Tike
 */

import java.net.*;
import java.io.*;

public class SimpleClient implements Runnable
{
    Thread clientThread;
    private String ipAddress;
    private int port = 8000;
    private int socketNum;
    Socket socket;
    private DataInputStream dis=null;
    
    public void setConnection(String ip) 
    {
        clientThread = new Thread(this);
        ipAddress = ip;
        clientThread.start();
    }
    
    @Override
    public void run()
    {
        try{
            socket = new Socket(ipAddress, port);
            socket.setSoTimeout(5000);
            dis = new DataInputStream(socket.getInputStream());
            String data = dis.readUTF();
            String splited[] = data.split("\\s+");
            
            Player.myID = Integer.parseInt(splited[0]);
            Framework.numOfPlayers = Integer.parseInt(splited[1]);
            for(int i=0;i<Framework.numOfPlayers;i++)
            {
                SimpleServer.clientIP[i] = splited[i+2];
            }
            
            dis.close();
            socket.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}