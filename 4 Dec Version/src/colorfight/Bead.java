package colorfight;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;
import java.awt.geom.Ellipse2D;

/**
 * @author Nisarg Tike
 */

public class Bead 
{
    public int dist;
    private Random randomLoc;
 
    public int xBead;
    public int yBead;
    public int beadRadius = 5;
    public int beadColor;		
    
	
    public int beadSnatch;	//0 indicates no points, 1 indicates p1 eats it, 2 indicates p2 eats it
    
    public Bead()
    {
        Initialize();
    }
    
    private void Initialize()
    {
        randomLoc = new Random();
        ResetBead();
    }
    
   
    public void ResetBead()
    {
        xBead = randomLoc.nextInt(1500)%1500;
        yBead = randomLoc.nextInt(1100)%1100;
		
        switch((xBead+yBead)%3)
        {
            case 0:
                beadColor = 1;
                break;
            case 1:
                beadColor = 2;
                break;
            case 2:
                beadColor = 3;
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
	
	
    public int UpdateBead(Player pSelf, Player[] peerPlayer)
    {
        beadSnatch = 0;
        if(collisionDetect(pSelf, xBead, yBead))
        {
            beadSnatch = pSelf.ID; //This machine has scored this bead
            pSelf.score();  //Check if score increases
            ResetBead();
        }
        else
        {
            for(int i=0;i<Framework.numOfPlayers-1;i++)
            {
                if(collisionDetect(peerPlayer[i], xBead, yBead))
                {
                    beadSnatch = peerPlayer[i].ID;
                    ResetBead();
                    break;
                }
            }
        }
        return beadSnatch;
    }
    
    
    public void Draw(Graphics2D g2d, Player pMac)
    {
        if(beadColor==1)
                g2d.setColor(Color.red);
        else if(beadColor==2)
                g2d.setColor(Color.green);
        else if(beadColor==3)
                g2d.setColor(Color.blue);

        Ellipse2D.Double circle;
            
        //Map player start
        if(pMac.x<=400 && pMac.y<=300)
            circle = new Ellipse2D.Double(xBead - beadRadius, yBead - beadRadius, 2*beadRadius, 2*beadRadius);
        else if(pMac.x>=1200-6 && pMac.y<=300)
            circle = new Ellipse2D.Double(xBead-800+6 - beadRadius, yBead - beadRadius, 2*beadRadius, 2*beadRadius);
        else if(pMac.x<=400 && pMac.y>=900-30)
            circle = new Ellipse2D.Double(xBead - beadRadius, yBead-600+30 - beadRadius, 2*beadRadius, 2*beadRadius);
        else if(pMac.x>=1200-6 && pMac.y>=900-30)
            circle = new Ellipse2D.Double(xBead-800+6 - beadRadius, yBead-600+30 - beadRadius, 2*beadRadius, 2*beadRadius);
        else if(pMac.x<=400)
            circle = new Ellipse2D.Double(xBead - beadRadius, yBead+300-pMac.y - beadRadius, 2*beadRadius, 2*beadRadius);
        else if(pMac.x>=1200-6)
            circle = new Ellipse2D.Double(xBead-800+6 - beadRadius, yBead+300-pMac.y - beadRadius, 2*beadRadius, 2*beadRadius);
        else if(pMac.y<=300)
            circle = new Ellipse2D.Double(xBead+400-pMac.x - beadRadius, yBead - beadRadius, 2*beadRadius, 2*beadRadius);
        else if(pMac.y>=900-30)
            circle = new Ellipse2D.Double(xBead+400-pMac.x - beadRadius, yBead-600+30 - beadRadius, 2*beadRadius, 2*beadRadius);
        else
            circle = new Ellipse2D.Double(xBead+400-pMac.x - beadRadius, yBead+300-pMac.y - beadRadius, 2*beadRadius, 2*beadRadius);
        //Map player end
        g2d.fill(circle);
    }
}
