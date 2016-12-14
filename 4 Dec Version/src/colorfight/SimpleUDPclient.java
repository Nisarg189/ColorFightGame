package colorfight;
 
/**
 * @author Nisarg Tike
 */

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;
 
public class SimpleUDPclient implements Runnable
{
    DatagramSocket sock = null;
    int port=8000, socketNum;
    InetAddress host;
    String s;
    public SimpleUDPclient(String serverName, int sockNum)
    {
        Thread client = new Thread(this);
        socketNum = sockNum;
        try
        {
            sock = new DatagramSocket();
            host = InetAddress.getByName(serverName);
            client.start();
        }
        catch(IOException e)
        {
            System.err.println("IOException " + e);
        }
    }
    
    @Override
    public void run()
    {
        while(true)
        {
            try{
                s = "OK";
                byte[] b = s.getBytes();
                //System.out.println("Creating a new datagram packet");
                DatagramPacket  dp = new DatagramPacket(b , b.length , host , port);
                sock.send(dp);
                byte[] buffer = new byte[65536];
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                sock.receive(reply);
                byte[] data = reply.getData();
                s = new String(data, 0, reply.getLength());
                    
                Game.recInfoReg[socketNum] = s;
                
                 try{
                    TimeUnit.MILLISECONDS.sleep(10);
                }
                catch(Exception e){}
            }catch(Exception e){
                System.out.println("Exception!");
            }
        }
    }
}