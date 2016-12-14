 package colorfight;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import java.awt.image.AffineTransformOp;

/**
 * @author Nisarg Tike
 */

public class Fireball {
    
    private Random randomLoc;
    private long lastTime= 0;
    public int xFireball;
    public int yFireball;
    private int maxFireballSpeed = 12;
    public int xFireballSpeed;
    public int yFireballSpeed;
    public boolean active = false;
    private BufferedImage fireBallImg;
    public int killID;
    public Sound fire = new Sound();
    double rotationRequired;
    
    public Sound collide = new Sound();
    
    Fireball()
    {
        LoadContent();
        xFireball = 0;
        yFireball = 0;
        active = false;
    }
    private void LoadContent()
    {
        try
        {
            fireBallImg = ImageIO.read(getClass().getResource("/images1/fireball.png"));
        }
        catch (IOException ex) {
            Logger.getLogger(Fireball.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private int setTarget(Player attacker, Player[] victim)
    {
        int distX, distY, dist, min, playerI=2;
        min = 3000; //A very big value
        for(int i=0;i<Framework.numOfPlayers-1;i++)
        {
            if(victim[i].alive)
            {
                distX = attacker.x-victim[i].x;
                if(distX<0)
                    distX=-1*distX;

                distY = attacker.y-victim[i].y;
                if(distY<0)
                    distY=-1*distY;

                dist = distX + distY;

                if(dist<=min)
                {
                    playerI = i;
                }
            }
        }
        return playerI;
    }
    
    private int setTarget(int xFB, int yFB, Player[] victim)
    {
        int distX, distY, dist, min, playerI=2;
        min = 3000; //A very big value
        for(int i=0;i<Framework.numOfPlayers-1;i++)
        {
            if(victim[i].alive)
            {
                distX = xFB - victim[i].x;
                if(distX<0)
                    distX=-1*distX;

                distY = yFB - victim[i].y;
                if(distY<0)
                    distY=-1*distY;

                dist = distX + distY;

                if(dist<=min)
                {
                    playerI = i;
                }
            }
        }
        return playerI;
    }
    
    public void shoot(Player attacker, Player victim[])
    {
        if(active==false && attacker.fireBallCapacity>0)
        {
            if(attacker.green || attacker.white)
            {
                fire.playSound("bullet2.wav");
                attacker.fireBallCapacity--;
                lastTime = System.nanoTime()/1000000000;
                xFireball = attacker.x;
                yFireball = attacker.y;
                int targetI = setTarget(attacker, victim);
                calcAngle(xFireball, yFireball, victim[targetI].x, victim[targetI].y);
                active = true;
            }
        }
    }
	
	public void calcAngle(int x1, int y1, int x2, int y2)		//Function yet to be revised at 90 deg
	{
		//Assuming Victim is at the center
		//Case 1			//1st Quadrant
		if(x1>x2 && y2>y1)
		{
			xFireballSpeed = (int)((-1*maxFireballSpeed)*(Math.cos(Math.atan(((float)y2-(float)y1)/((float)x1-(float)x2)))));
			yFireballSpeed = (int)(maxFireballSpeed*(Math.sin(Math.atan(((float)y2-(float)y1)/((float)x1-(float)x2)))));
		}
		//Case 2		//2nd Quadrant
		if(x1<x2 && y2>y1)
		{
			xFireballSpeed = (int)(maxFireballSpeed*(Math.cos(Math.atan(((float)y2-(float)y1)/((float)x2-(float)x1)))));
			yFireballSpeed = (int)(maxFireballSpeed*(Math.sin(Math.atan(((float)y2-(float)y1)/((float)x2-(float)x1)))));
		}
		//Case 3		//3rd Quadrant
		if(x1<x2 && y1>y2)
		{
			xFireballSpeed = (int)(maxFireballSpeed*(Math.cos(Math.atan(((float)y1-(float)y2)/((float)x2-(float)x1)))));
			yFireballSpeed = (int)((-1*maxFireballSpeed)*(Math.sin(Math.atan(((float)y1-(float)y2)/((float)x2-(float)x1)))));
		}
		//Case 4		//4th Quadrant
		if(x1>x2 && y1>y2)
		{
			xFireballSpeed = (int)((-1*maxFireballSpeed)*(Math.cos(Math.atan(((float)y1-(float)y2)/((float)x1-(float)x2)))));
			yFireballSpeed = (int)((-1*maxFireballSpeed)*(Math.sin(Math.atan(((float)y1-(float)y2)/((float)x1-(float)x2)))));
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
	
	
    public int updateFireBall(Player[] peerPlayer)
    {
        killID = 0;		//Nothing happened
	if(active)
        {
            rotationRequired += 1.3;
            if(System.nanoTime()/1000000000 - lastTime>10)
                active = false;
            else
            {
                for(int i=0;i<Framework.numOfPlayers-1;i++)
                {
                    if(collisionDetect(peerPlayer[i], xFireball, yFireball))
                    {
                        collide.playSound("blast.wav");
                        killID = peerPlayer[i].ID;			//P2 died
                        active = false;
                    }
                }
                if(active)		//Out of frame
                {
                    int targetI = setTarget(xFireball, yFireball, peerPlayer);
                    calcAngle(xFireball, yFireball, peerPlayer[targetI].x, peerPlayer[targetI].y);
                    xFireball += xFireballSpeed;
                    yFireball += yFireballSpeed;

                    if(xFireball>1600 || xFireball<-30 || yFireball>1200 || yFireball<-30)
                        active = false;
                }
            }
        }
        else
        {
            rotationRequired = 0;
        }
        return killID;
    }
    public void updatePeerFireball(String info)
    {
        String[] splited = info.split("\\s+");  //space as delimeter
        xFireball = Integer.parseInt(splited[7]);
        yFireball = Integer.parseInt(splited[8]);
        int check = Integer.parseInt(splited[9]);
        if(check==0)
        {
            active = false;
            rotationRequired = 0;
        }
        else if(check==1)
        {
            active = true;
            rotationRequired += 1.3;
        }
    }
    public void Draw(Graphics2D g2d)
    {
        if(active)
        {
            double locationX = fireBallImg.getWidth()/2;
            double locationY = fireBallImg.getHeight()/2;
            AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
            g2d.drawImage(op.filter(fireBallImg, null), xFireball, yFireball, null);
        }
    }
    
    public void Draw(Graphics2D g2d, Player pMac)
    {       
        
        if(active)
        {
            double locationX = fireBallImg.getWidth()/2;
            double locationY = fireBallImg.getHeight()/2;
            AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
            AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
            
            //Map player start
            if(pMac.x<=400 && pMac.y<=300)
            {
                g2d.drawImage(op.filter(fireBallImg, null), xFireball, yFireball, null);
            }
            else if(pMac.x>=1200-6 && pMac.y<=300)
            {
                g2d.drawImage(op.filter(fireBallImg, null), xFireball-800+6, yFireball, null);
            }
            else if(pMac.x<=400 && pMac.y>=900-30)
            {
                g2d.drawImage(op.filter(fireBallImg, null), xFireball, yFireball-600+30, null);
            }
            else if(pMac.x>=1200-6 && pMac.y>=900-30)
            {
                g2d.drawImage(op.filter(fireBallImg, null), xFireball-800+6, yFireball-600+30, null);
            }
            else if(pMac.x<=400)
            {
                g2d.drawImage(op.filter(fireBallImg, null), xFireball, yFireball+300-pMac.y, null);
            }
            else if(pMac.x>=1200-6)
            {
                g2d.drawImage(op.filter(fireBallImg, null), xFireball-800+6, yFireball+300-pMac.y, null);
            }
            else if(pMac.y<=300)
            {
                g2d.drawImage(op.filter(fireBallImg, null), xFireball+400-pMac.x, yFireball, null);
            }
            else if(pMac.y>=900-30)
            {
                g2d.drawImage(op.filter(fireBallImg, null), xFireball+400-pMac.x, yFireball-600+30, null);
            }
            else
            {
                g2d.drawImage(op.filter(fireBallImg, null), xFireball+400-pMac.x, yFireball+300-pMac.y, null);
            }
            //Map player end
        }
    }
}