package colorfight;

/**
 * @author Nisarg Tike
 */

import java.net.*;
import java.io.*;

public class SimpleServer implements Runnable 
{
    private static int maxSupport = 30;
    private int port=8000;
    
    public static String[] clientIP = new String[maxSupport];
    private Socket[] socket = new Socket[maxSupport];
    private DataOutputStream[] dos = new DataOutputStream[maxSupport];
    private DataInputStream[] dis = new DataInputStream[maxSupport];
    
    Thread serverThread;
    
    public void acceptConnection() 
    {
        serverThread = new Thread(this);
        serverThread.start();
    }

    @Override
    public void run() {
        try {
            String clientIPinfo = Framework.machineIP;
            ServerSocket server = new ServerSocket(port);
            for(int i=0;i<Framework.numOfPlayers-1;i++)
            {
                socket[i] = server.accept();
                clientIP[i+1] = socket[i].getInetAddress().toString().substring(1);
                dos[i] = new DataOutputStream(socket[i].getOutputStream());
                dis[i] = new DataInputStream(socket[i].getInputStream());
            }
            
            for(int i=1;i<Framework.numOfPlayers;i++)
            {
                clientIPinfo += " "+clientIP[i];
            }
            
            //Send critical info to all peers
            for(int i=0;i<Framework.numOfPlayers-1;i++)
            {
                dos[i].writeUTF(Integer.toString(i+2) + " " +Integer.toString(Framework.numOfPlayers) +" "+ clientIPinfo);
                dos[i].flush();
                dos[i].close();
                dis[i].close();
                socket[i].close();
            }
        }catch(Exception e){}
    }
}
