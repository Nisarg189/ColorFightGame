package colorfight; 

/**
 * @author Nisarg Tike
 */

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;
 
public class SimpleUDPserver implements Runnable
{
    DatagramSocket sock =null;
    DatagramPacket incoming;
    int port = 8000;
    public SimpleUDPserver()
    {
        Thread server = new Thread(this);
        try
        {
            sock = new DatagramSocket(port);
            byte[] buffer = new byte[65536];
            incoming = new DatagramPacket(buffer, buffer.length);
            server.start();
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
                sock.receive(incoming);
                byte[] data = incoming.getData();
                String s = new String(data, 0, incoming.getLength());

                s = Game.sendInfoReg;
                DatagramPacket dp = new DatagramPacket(s.getBytes() , s.getBytes().length , incoming.getAddress() , incoming.getPort());
                sock.send(dp);
                
                 try{
                    TimeUnit.MILLISECONDS.sleep(10);
                }
                catch(Exception e){}
            }catch(Exception e){}
        }
    }
}