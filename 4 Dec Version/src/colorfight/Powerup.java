package colorfight;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * @author Nisarg Tike
 */

public class Powerup {
    
    public int dist;
    private Random randomLoc;
    private BufferedImage fireBallImg;
    private BufferedImage shieldImg;
    public int xPowerup;
    public int yPowerup;
    public long lastTimeActive=0;
    public boolean fire;            //If powerup is fire
    public boolean shield;          //If powerup is shield
	
    public int powerupSnatch;	//0 indicates no one took, 1 indicates p1 took, 2 indicates p2 took
    
    public Powerup()
    {
        Initialize();
        LoadContent();
    }
    
    
    private void Initialize()
    {
        randomLoc = new Random();
        ResetPowerup();
    }
    
    private void LoadContent()
    {
        try
        {
            shieldImg = ImageIO.read(getClass().getResource("/images1/shield.png"));
            fireBallImg = ImageIO.read(getClass().getResource("/images1/fireballImg.png"));
        }
        catch (IOException ex) {
            Logger.getLogger(Powerup.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    public void ResetPowerup()
    {
        fire = false;
        shield = false;
		
        xPowerup = randomLoc.nextInt(1500)%1500;
        yPowerup = randomLoc.nextInt(1100)%1100;
		
        switch((xPowerup+yPowerup)%2)
        {
            case 0:
                fire = true;
                break;
            case 1:
                shield = true;
                break;
        }
    }
    
    
	public boolean collisionDetect(Player p, int x2, int y2)
	{
            boolean check;
            int distX = p.x - x2;
            int distY = p.y - y2;
            if(distX<0)
                    distX = (-1)*distX;
            if(distY<0)
                    distY = (-1)*distY;

            if(distX<p.playerRadius && distY<p.playerRadius)
                    check = true;
            else
                    check = false;

            return check;
	}
        
    public int UpdatePowerup(Player pSelf, Player[] peerPlayer)
    {
        powerupSnatch = 0;      //0 indicates nothing
        if(fire)
        {
            if(collisionDetect(pSelf, xPowerup, yPowerup) && (pSelf.green || pSelf.white))
            {
                powerupSnatch = pSelf.ID;
                pSelf.fireBall();
                lastTimeActive = System.nanoTime()/1000000000;
                ResetPowerup();
            }
            else 
            {
                for(int i=0;i<Framework.numOfPlayers-1;i++)
                {
                    if(collisionDetect(peerPlayer[i], xPowerup, yPowerup) && (peerPlayer[i].white || peerPlayer[i].green))
                    {
                        powerupSnatch = peerPlayer[i].ID;
                        peerPlayer[i].fireBall();
                        lastTimeActive = System.nanoTime()/1000000000;
                        //ResetPowerup();
                        break;
                    }
                }
            }
        }
        else if(shield)
        {
            if(collisionDetect(pSelf, xPowerup, yPowerup) && (!pSelf.orange))
            {
                powerupSnatch = pSelf.ID+Framework.numOfPlayers;
                pSelf.shield();
                lastTimeActive = System.nanoTime()/1000000000;
                ResetPowerup();
            }
            else 
            {
                for(int i=0;i<Framework.numOfPlayers-1;i++)
                {
                    if(collisionDetect(peerPlayer[i], xPowerup, yPowerup) && (!peerPlayer[i].orange))
                    {
                        powerupSnatch = peerPlayer[i].ID+Framework.numOfPlayers;
                        peerPlayer[i].shield();
                        lastTimeActive = System.nanoTime()/1000000000;
                        //ResetPowerup();
                        break;
                    }
                }
            }
        }
        return powerupSnatch;
    }
    
    public void Draw(Graphics2D g2d, Player pMac)
    {       
        //Map player start
        if(pMac.x<=400 && pMac.y<=300)
        {
            if(fire)
                g2d.drawImage(fireBallImg, xPowerup, yPowerup, null);
            else if(shield)
                g2d.drawImage(shieldImg, xPowerup, yPowerup, null);
        }
        else if(pMac.x>=1200-6 && pMac.y<=300)
        {
            if(fire)
                g2d.drawImage(fireBallImg, xPowerup-800+6, yPowerup, null);
            else if(shield)
                g2d.drawImage(shieldImg, xPowerup-800+6, yPowerup, null);
        }
        else if(pMac.x<=400 && pMac.y>=900-30)
        {
            if(fire)
                g2d.drawImage(fireBallImg, xPowerup, yPowerup-600+30, null);
            else if(shield)
                g2d.drawImage(shieldImg, xPowerup, yPowerup-600+30, null);
        }
        else if(pMac.x>=1200-6 && pMac.y>=900-30)
        {
            if(fire)
                g2d.drawImage(fireBallImg, xPowerup-800+6, yPowerup-600+30, null);
            else if(shield)
                g2d.drawImage(shieldImg, xPowerup-800+6, yPowerup-600+30, null);
        }
        else if(pMac.x<=400)
        {
            if(fire)
                g2d.drawImage(fireBallImg, xPowerup, yPowerup+300-pMac.y, null);
            else if(shield)
                g2d.drawImage(shieldImg, xPowerup, yPowerup+300-pMac.y, null);
        }
        else if(pMac.x>=1200-6)
        {
            if(fire)
                g2d.drawImage(fireBallImg, xPowerup-800+6, yPowerup+300-pMac.y, null);
            else if(shield)
                g2d.drawImage(shieldImg, xPowerup-800+6, yPowerup+300-pMac.y, null);
        }
        else if(pMac.y<=300)
        {
            if(fire)
                g2d.drawImage(fireBallImg, xPowerup+400-pMac.x, yPowerup, null);
            else if(shield)
                g2d.drawImage(shieldImg, xPowerup+400-pMac.x, yPowerup, null);
        }
        else if(pMac.y>=900-30)
        {
            if(fire)
                g2d.drawImage(fireBallImg, xPowerup+400-pMac.x, yPowerup-600+30, null);
            else if(shield)
                g2d.drawImage(shieldImg, xPowerup+400-pMac.x, yPowerup-600+30, null);
        }
        else
        {
            if(fire)
                g2d.drawImage(fireBallImg, xPowerup+400-pMac.x, yPowerup+300-pMac.y, null);
            else if(shield)
                g2d.drawImage(shieldImg, xPowerup+400-pMac.x, yPowerup+300-pMac.y, null);
        }
        //Map player end
    }
}