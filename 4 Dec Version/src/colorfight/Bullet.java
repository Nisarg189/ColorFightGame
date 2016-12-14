package colorfight;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;
import java.awt.geom.Ellipse2D;

/**
 * @author Nisarg Tike
 */

public class Bullet {
    
    private Random randomLoc;
 
    public int xBullet;
    public int yBullet;
    public int maxBulletSpeed = 12;
    public int xBulletSpeed;
    public int yBulletSpeed;
    public boolean active = false;
    
    private int killID;	//1 indicates p1 shoots, 2 indicates p2 shoots
    
    public Sound bulletSound = new Sound();
    
    public Sound collide = new Sound();
    
    public Bullet()
    {
        xBullet = 0;
        yBullet = 0;
        active = false;
    }
    private int setTarget(Player attacker, Player[] victim)
    {
        int distX, distY, dist, min, playerI=-1;
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
                    min = dist;
                    playerI = i;
                }
            }
        }
        return playerI;
    }
    
    public void shoot(Player attacker, Player[] victim)
    {
        if(active==false)
        {
            if(attacker.green || attacker.white)
            {
                xBullet = attacker.x;
                yBullet = attacker.y;
                bulletSound.playSound("bullet.wav");
                int targetI = setTarget(attacker, victim);
                calcAngle(attacker.x, attacker.y, victim[targetI].x, victim[targetI].y);
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
            xBulletSpeed = (int)((-1*maxBulletSpeed)*(Math.cos(Math.atan(((float)y2-(float)y1)/((float)x1-(float)x2)))));
            yBulletSpeed = (int)(maxBulletSpeed*(Math.sin(Math.atan(((float)y2-(float)y1)/((float)x1-(float)x2)))));
        }
        //Case 2		//2nd Quadrant
        if(x1<x2 && y2>y1)
        {
            xBulletSpeed = (int)(maxBulletSpeed*(Math.cos(Math.atan(((float)y2-(float)y1)/((float)x2-(float)x1)))));
            yBulletSpeed = (int)(maxBulletSpeed*(Math.sin(Math.atan(((float)y2-(float)y1)/((float)x2-(float)x1)))));
        }
        //Case 3		//3rd Quadrant
        if(x1<x2 && y1>y2)
        {
            xBulletSpeed = (int)(maxBulletSpeed*(Math.cos(Math.atan(((float)y1-(float)y2)/((float)x2-(float)x1)))));
            yBulletSpeed = (int)((-1*maxBulletSpeed)*(Math.sin(Math.atan(((float)y1-(float)y2)/((float)x2-(float)x1)))));
        }
        //Case 4		//4th Quadrant
        if(x1>x2 && y1>y2)
        {
            xBulletSpeed = (int)((-1*maxBulletSpeed)*(Math.cos(Math.atan(((float)y1-(float)y2)/((float)x1-(float)x2)))));
            yBulletSpeed = (int)((-1*maxBulletSpeed)*(Math.sin(Math.atan(((float)y1-(float)y2)/((float)x1-(float)x2)))));
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
	
	
    public int updateMyBullet(Player[] peerPlayer)
    {
        killID = 0;		//Nothing happened
	if(active)
        {
            for(int i=0;i<Framework.numOfPlayers-1;i++)
            {
                if(collisionDetect(peerPlayer[i], xBullet, yBullet))
                {
                    collide.playSound("blast.wav");
                    if(!peerPlayer[i].white && !peerPlayer[i].shield)
                        killID = peerPlayer[i].ID;			//peerPlayer[i] died
                    else if(!peerPlayer[i].white && peerPlayer[i].shield)
                        killID = peerPlayer[i].ID + Framework.numOfPlayers;
                    active = false;
                }
            }
            
            if(active)		//Out of frame
            {
                xBullet += xBulletSpeed;
                yBullet += yBulletSpeed;

                if(xBullet>1600 || xBullet<-30 || yBullet>1200 || yBullet<-30)
                    active = false;
            }
        }
        return killID;
    }
    public void updatePeerBullet(String info)
    {
        try{
            String[] splited = info.split("\\s+");  //space as delimeter
            xBullet = Integer.parseInt(splited[4]);
            yBullet = Integer.parseInt(splited[5]);
            int check = Integer.parseInt(splited[6]);
            if(check==0)
                active = false;
            else if(check==1)
                active = true;
        }
        catch(Exception e){}
    }
    
    public void Draw(Graphics2D g2d, Player pMac)
    {       
        
        if(active)
        {
            g2d.setColor(Color.black);
            Ellipse2D.Double circle;
            //Map player start
            if(pMac.x<=400 && pMac.y<=300)
            {
                circle = new Ellipse2D.Double(xBullet, yBullet, 5, 5);
            }
            else if(pMac.x>=1200-6 && pMac.y<=300)
            {
                circle = new Ellipse2D.Double(xBullet-800+6, yBullet, 5, 5);
            }
            else if(pMac.x<=400 && pMac.y>=900-30)
            {
                circle = new Ellipse2D.Double(xBullet, yBullet-600+30, 5, 5);
            }
            else if(pMac.x>=1200-6 && pMac.y>=900-30)
            {
                circle = new Ellipse2D.Double(xBullet-800+6, yBullet-600+30, 5, 5);
            }
            else if(pMac.x<=400)
            {
                circle = new Ellipse2D.Double(xBullet, yBullet+300-pMac.y, 5, 5);
            }
            else if(pMac.x>=1200-6)
            {
                circle = new Ellipse2D.Double(xBullet-800+6, yBullet+300-pMac.y, 5, 5);
            }
            else if(pMac.y<=300)
            {
                circle = new Ellipse2D.Double(xBullet+400-pMac.x, yBullet, 5, 5);
            }
            else if(pMac.y>=900-30)
            {
                circle = new Ellipse2D.Double(xBullet+400-pMac.x, yBullet-600+30, 5, 5);
            }
            else
            {
                circle = new Ellipse2D.Double(xBullet+400-pMac.x, yBullet+300-pMac.y, 5, 5);
            }
            //Map player end
            g2d.fill(circle);
        }
    }
}