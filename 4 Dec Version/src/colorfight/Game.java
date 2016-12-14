package colorfight;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.util.concurrent.TimeUnit;

/**
 * @author Nisarg Tike
 */

public class Game {
    
    public Player pMe;      //This machine's player
    public Player[] peerPlayer;         //Other players
    
    private int playerWon=0;
    
    //Members for network
    String serverName;        //This will be fetched from Framework
    public static String sendInfo;      //This is the string to be sent to all clients
    public static String sendInfoReg;
    public static String[] recInfo;      //This is the string to be recieved
    public static String[] recInfoReg;      //This is the string to be recieved

    private int bulletKillID;
    private int fireBallKillID;
    
    //These two are shared resource//will be handled by Master Node
    private Bead[] bead;
    private Powerup[] powerup;
    
    private BufferedImage[][] map;
    
    public Bullet myBullet;
    public Bullet[] peerBullet;
    public Fireball myFireball;
    public Fireball[] peerFireball;
    
    public Game()
    {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
        
        Thread threadForInitGame = new Thread() {
            @Override
            public void run(){
                
                ConnectPeer();
                Initialize();
                LoadContent();
                
                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }
    
    
   /**
     * Set variables and objects for the game.
     */
    
    
    private void ConnectPeer()
    {
        if(Framework.join==1)
        {
            pMe = new Player();
            pMe.ID = pMe.myID = 1; //Master ID is 1
            
            SimpleServer.clientIP[0] = Framework.machineIP;
            
            SimpleServer serverMac = new SimpleServer();
            serverMac.acceptConnection();
            
            recInfoReg = new String[Framework.numOfPlayers-1];
            sendInfoReg = "1 20 20 1 20 20 0 20 20 0 0 40 40 1 0 50 50 1 0 0 0 0 0"; //length=21
            
            //This machine will start its UDP servers now
            SimpleUDPserver ser = new SimpleUDPserver();
           
            //Wait for sometime and then start client
            try{
                TimeUnit.SECONDS.sleep(25);
            }
            catch(Exception e){}
            
            SimpleUDPclient[] cli = new SimpleUDPclient[Framework.numOfPlayers-1];
            for(int i=0;i<Framework.numOfPlayers-1;i++)
            {
                cli[i] = new SimpleUDPclient(SimpleServer.clientIP[i+1], i);
            }
            //This machine is connected with all peers
        }
        else if(Framework.join==0)
        {
            pMe = new Player();
            //Let server start its server sockets//Sleep for 2 seconds
            try{
                TimeUnit.SECONDS.sleep(2);
            }
            catch(Exception e){}
            
            SimpleClient clientM = new SimpleClient();
            
            clientM.setConnection(Framework.serverName);
            
            try{
                TimeUnit.SECONDS.sleep(5);
            }
            catch(Exception e){}
            
            //At this instant, this machine has all required info
            pMe.ID = pMe.myID;
            
            recInfo = new String[Framework.numOfPlayers-1];
            recInfoReg = new String[Framework.numOfPlayers-1];
            sendInfoReg = "1 20 20 1 20 20 0 20 20 0 0 40 40 1 0 50 50 1 0 0 0 0 0"; //length=21
            
            //start UDP server
            SimpleUDPserver ser = new SimpleUDPserver();
            
            //Wait for sometime and then start client
            try{
                TimeUnit.SECONDS.sleep(25);
            }
            catch(Exception e){}
            
            
            SimpleUDPclient[] cli = new SimpleUDPclient[Framework.numOfPlayers-1];
            for(int i=0;i<Framework.numOfPlayers;i++)
            {
                if(i<pMe.ID-1)
                    cli[i] = new SimpleUDPclient(SimpleServer.clientIP[i], i);
                else if(i>=pMe.ID)
                    cli[i-1] = new SimpleUDPclient(SimpleServer.clientIP[i], i-1);
            }
            //This machine is connected with all peers
        }
    }
    
    private void Initialize()
    {
        peerPlayer = new Player[Framework.numOfPlayers-1];
        for(int i=0;i<Framework.numOfPlayers;i++)
        {
            if(i<pMe.ID-1)
            {
                peerPlayer[i] = new Player();
                peerPlayer[i].ID = i+1;
            }
            else if(i>=pMe.ID)
            {
                peerPlayer[i-1] = new Player();
                peerPlayer[i-1].ID = i+1; 
            }
        }
        
        bead = new Bead[8];
        for(int i=0;i<8;i++)
            bead[i] = new Bead();
        bead[0].xBead = 100;
        bead[0].yBead = 120;
        bead[1].xBead = 10;
        bead[1].yBead = 130;
        bead[2].xBead = 130;
        bead[2].yBead = 20;
        bead[3].xBead = 300;
        bead[3].yBead = 320;
        bead[4].xBead = 330;
        bead[4].yBead = 12;
        bead[5].xBead = 190;
        bead[5].yBead = 220;
        bead[6].xBead = 260;
        bead[6].yBead = 120;
        bead[7].xBead = 400;
        bead[7].yBead = 100;
        
        powerup = new Powerup[4];
        for(int i=0;i<4;i++)
            powerup[i] = new Powerup(); //Check this
        
        powerup[0].xPowerup = 104;
        powerup[0].yPowerup = 104;
        powerup[0].fire = true;
        powerup[0].shield = false;
        
        powerup[1].xPowerup = 30;
        powerup[1].yPowerup = 104;
        powerup[1].fire = true;
        powerup[1].shield = false;
        
        powerup[2].xPowerup = 20;
        powerup[2].yPowerup = 200;
        powerup[2].fire = false;
        powerup[2].shield = true;
        
        powerup[3].xPowerup = 240;
        powerup[3].yPowerup = 140;
        powerup[3].fire = false;
        powerup[3].shield = true;
        
        myBullet = new Bullet();
        peerBullet = new Bullet[Framework.numOfPlayers-1];
        
        myFireball = new Fireball();
        peerFireball = new Fireball[Framework.numOfPlayers-1];
        
        for(int i=0;i<Framework.numOfPlayers-1;i++)
        {
            peerBullet[i] = new Bullet();
            peerFireball[i] = new Fireball();
        }
    }
    
    private void LoadContent()
    {
        try
        {
            map = new BufferedImage[2][2];
            for(int i=0;i<2;i++)
            {
                for(int j=0;j<2;j++)
                {
                    map[i][j] = ImageIO.read(getClass().getResource("/images1/map/"+i+j+".jpg"));
                }
            }
        }
        catch (IOException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void RestartGame()
    {
        pMe.ResetPlayer();
        myBullet.active = false;
        myFireball.active = false;
        Initialize();
    }
    
    public void UpdateGame(long gameTime, Point mousePosition)
    {
        //We continuously recieve recInfo//It is updated
        for(int i=0;i<Framework.numOfPlayers-1;i++)
        {
            String reci;
            try{
                reci = recInfoReg[i];
            }catch(Exception e)
            {
                //Reci: Alive,Px,Py,Pcol,Bx,By,BAct,FBx,FBy,FBAct,NewBeadI,X,Y,col,newPwer,X,Y,type 
                reci = "1 20 20 1 20 20 0 20 20 0 0 40 40 1 0 50 50 1 0 0 0 0 0"; //length=21
            }
            if(reci==null)
                reci = "1 20 20 1 20 20 0 20 20 0 0 40 40 1 0 50 50 1 0 0 0 0 0"; //length=21
            String[] splitKill = reci.split("\\s+");
            if(Integer.parseInt(splitKill[21])==pMe.ID || Integer.parseInt(splitKill[22])==pMe.ID)
                pMe.alive=false;
            else if(Integer.parseInt(splitKill[21])==pMe.ID+Framework.numOfPlayers)
                pMe.shield = false;
            peerPlayer[i].UpdatePeerPlayer(reci);
            peerBullet[i].updatePeerBullet(reci);
            peerFireball[i].updatePeerFireball(reci);
            //Updating beads
            String[] commonResource = reci.split("\\s+");
            if(Integer.parseInt(commonResource[10])>0)
            {
                bead[Integer.parseInt(commonResource[10])-1].xBead = Integer.parseInt(commonResource[11]);
                bead[Integer.parseInt(commonResource[10])-1].yBead = Integer.parseInt(commonResource[12]);
                bead[Integer.parseInt(commonResource[10])-1].beadColor = Integer.parseInt(commonResource[13]);
            }
            if(Integer.parseInt(commonResource[14])>0)
            {
                powerup[Integer.parseInt(commonResource[14])-1].xPowerup = Integer.parseInt(commonResource[15]);
                powerup[Integer.parseInt(commonResource[14])-1].yPowerup = Integer.parseInt(commonResource[16]);
                if(Integer.parseInt(commonResource[17])==1)
                {
                    powerup[Integer.parseInt(commonResource[14])-1].shield = false;
                    powerup[Integer.parseInt(commonResource[14])-1].fire = true;
                }
                else if(Integer.parseInt(commonResource[17])==2)
                {
                    powerup[Integer.parseInt(commonResource[14])-1].fire = false;
                    powerup[Integer.parseInt(commonResource[14])-1].shield = true;
                }
            }
        }
        
        pMe.UpdateSelf();   //Current machine player is updated
        
        if(pMe.checkShooting())		//Shoot logic
            myBullet.shoot(pMe, peerPlayer);
        bulletKillID = myBullet.updateMyBullet(peerPlayer);
        if(pMe.checkFire())
            myFireball.shoot(pMe, peerPlayer);
        fireBallKillID = myFireball.updateFireBall(peerPlayer);
        
        
        //Encoding MESSAGE
        String str0="";
        if(pMe.alive)
            str0 = "1";
        else
            str0 = "0";
        String str1 = Integer.toString(pMe.x);
        String str2 = Integer.toString(pMe.y);
        int color=1;
        if(pMe.blue)
            color = 2;
        else if(pMe.green)
            color = 3;
        else if(pMe.white)
            color = 4;
        String str3 = Integer.toString(color);
        String str4 = Integer.toString(myBullet.xBullet);
        String str5 = Integer.toString(myBullet.yBullet);
        int checkBulletActive=0;
        if(myBullet.active)
            checkBulletActive=1;
        String str6 = Integer.toString(checkBulletActive);
        String str7 = Integer.toString(myFireball.xFireball);
        String str8 = Integer.toString(myFireball.yFireball);
        int checkBallActive=0;
        if(myFireball.active)
            checkBallActive=1;
        String str9 = Integer.toString(checkBallActive);
        String str10 = "";  //For bead info
        int outFor=0;
        for(int i=0;i<8;i++)
        {
            int scorer = bead[i].UpdateBead(pMe, peerPlayer);
            if(scorer==pMe.ID)
            {
                str10 = (i+1)+" "+bead[i].xBead+" "+bead[i].yBead+" "+bead[i].beadColor;
                outFor=1;
            }
            else if(outFor!=1)
            {
                str10 = "0 0 0 1";
            }
        }
        String str11 = "";
        int outFor1=0;
        for(int i=0;i<4;i++)
        {
            int scorer = powerup[i].UpdatePowerup(pMe, peerPlayer);
            if(scorer==pMe.ID || scorer==pMe.ID+Framework.numOfPlayers)
            {
                if(powerup[i].fire)
                {
                    str11 = (i+1)+" "+powerup[i].xPowerup+" "+powerup[i].yPowerup+" "+"1";
                    outFor1=1;
                }
                else if(powerup[i].shield)
                {
                    str11 = (i+1)+" "+powerup[i].xPowerup+" "+powerup[i].yPowerup+" "+"2";
                    outFor1=1;
                }
            }
            else if(outFor1!=1)
            {
                str11 = "0 0 0 1";
            }
        }
        String str12=Integer.toString(pMe.score);
        String str13="0";
        if(pMe.shield)
            str13="1";
        String str14=Integer.toString(pMe.fireBallCapacity);
        String str15=Integer.toString(bulletKillID);
        String str16=Integer.toString(fireBallKillID);
        sendInfoReg = str0+" "+str1+" "+str2+" "+str3+" "+str4+" "+str5+" "+str6+" "+str7+" "+str8+" "+str9+" "+str10+" "+str11+" "+str12+" "+str13+" "+str14+" "+str15+" "+str16;
        
        int dead=0;
        for(int i=0;i<Framework.numOfPlayers-1;i++)
        {
            if(!peerPlayer[i].alive)
                dead++;
        }
        if((pMe.alive && dead==Framework.numOfPlayers-1) || (!pMe.alive && dead==Framework.numOfPlayers-2))
        {
            for(int i=0;i<Framework.numOfPlayers-1;i++)
            {
                if(peerPlayer[i].alive)
                    playerWon = peerPlayer[i].ID;
            }
            if(pMe.alive)
                playerWon = pMe.ID;
            Framework.gameState = Framework.GameState.GAMEOVER;
        }   
    }
    
    public void Draw(Graphics2D g2d, Point mousePosition)
    {
        //MAP part starts
        if(pMe.x<=400 && pMe.y<=300)
        {
            g2d.drawImage(map[0][0], 0 ,0, Framework.frameWidth, Framework.frameHeight, null);
        }
        else if(pMe.x>=1200-6 && pMe.y<=300)
            g2d.drawImage(map[0][1], 0 ,0, Framework.frameWidth, Framework.frameHeight, null);
        else if(pMe.x>=1200-6 && pMe.y>=900-30)
            g2d.drawImage(map[1][1], 0 ,0, Framework.frameWidth, Framework.frameHeight, null);
        else if(pMe.x<=400 && pMe.y>=900-30)
            g2d.drawImage(map[1][0], 0 ,0, Framework.frameWidth, Framework.frameHeight, null);
        else if(pMe.x<=400) //2 3 quadrant
        {
            g2d.drawImage(map[Math.abs(pMe.y-300)/600][0], 0, -Math.abs(pMe.y-300)%600, Framework.frameWidth, Framework.frameHeight, null);
            g2d.drawImage(map[Math.abs(pMe.y+300)/600][0], 0, 600-30-Math.abs(pMe.y+300)%600, Framework.frameWidth, Framework.frameHeight, null);
            //peerPlayer[0].paintComponent(g2d, peerPlayer[0].x-pMe.x, peerPlayer[0].y-pMe.y, peerPlayer[0].playerRadius);
        }
        else if(pMe.x>=1200-6)    //1 4 quadrant
        {
            g2d.drawImage(map[Math.abs(pMe.y-300)/600][1], 0, -Math.abs(pMe.y-300)%600, Framework.frameWidth, Framework.frameHeight, null);
            g2d.drawImage(map[Math.abs(pMe.y+300)/600][1], 0, 600-30-Math.abs(pMe.y+300)%600, Framework.frameWidth, Framework.frameHeight, null);
        }
        else if(pMe.y<=300)     //1 2 quadrant
        {
            g2d.drawImage(map[0][Math.abs(pMe.x+400)/800], 800-6-Math.abs(pMe.x+400)%800, 0, Framework.frameWidth, Framework.frameHeight, null);
            g2d.drawImage(map[0][Math.abs(pMe.x-400)/800], -Math.abs(pMe.x-400)%800, 0, Framework.frameWidth, Framework.frameHeight, null);
        }
        else if(pMe.y>=900-30)     //3 4 quadrant
        {
            g2d.drawImage(map[1][Math.abs(pMe.x-400)/800], -Math.abs(pMe.x-400)%800, 0, Framework.frameWidth, Framework.frameHeight, null);
            g2d.drawImage(map[1][Math.abs(pMe.x+400)/800], 800-6-Math.abs(pMe.x+400)%800, 0, Framework.frameWidth, Framework.frameHeight, null);
        }
        else
        {
            g2d.drawImage(map[Math.abs(pMe.x-400)/800][Math.abs(pMe.y-300)/600], -Math.abs(pMe.x-400)%800 , -Math.abs(pMe.y-300)%600, Framework.frameWidth, Framework.frameHeight, null);
            g2d.drawImage(map[Math.abs(pMe.x+400)/800][Math.abs(pMe.y-300)/600], -Math.abs(pMe.x-400)%800 , 600-30-Math.abs(pMe.y+300)%600, Framework.frameWidth, Framework.frameHeight, null);
            g2d.drawImage(map[Math.abs(pMe.x-400)/800][Math.abs(pMe.y+300)/600], 800-6-Math.abs(pMe.x+400)%800, -Math.abs(pMe.y-300)%600, Framework.frameWidth, Framework.frameHeight, null);
            g2d.drawImage(map[Math.abs(pMe.x+400)/800][Math.abs(pMe.y+300)/600], 800-6-Math.abs(pMe.x+400)%800, 600-30-Math.abs(pMe.y+300)%600, Framework.frameWidth, Framework.frameHeight, null);
        }
        //MAP Part done
	for(int i=0;i<8;i++)
            bead[i].Draw(g2d, pMe);
        
        for(int i=0;i<4;i++)
            powerup[i].Draw(g2d, pMe);
        
        myBullet.Draw(g2d, pMe);
        myFireball.Draw(g2d, pMe);
        pMe.Draw(g2d);
        //peerPlayer[0].DrawE(g2d, pMe);
        for(int i=0;i<Framework.numOfPlayers-1;i++)
        {
            peerBullet[i].Draw(g2d, pMe);
            peerFireball[i].Draw(g2d, pMe);
            peerPlayer[i].DrawE(g2d, pMe);
        }
    }
    
    
    public void DrawGameOver(Graphics2D g2d, Point mousePosition, long gameTime)
    {
        Draw(g2d, mousePosition);
        g2d.drawString("Player "+playerWon+" Won!!!", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3 + 70);
	
        g2d.drawString("Press space or enter to restart.", Framework.frameWidth / 2 - 100, Framework.frameHeight / 3 + 100);
    }
}