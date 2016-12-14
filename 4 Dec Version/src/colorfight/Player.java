package colorfight;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.Ellipse2D;
import java.util.Random;
import javax.swing.JComponent;

/**
 * @author Nisarg Tike
 */

public class Player extends JComponent{
    
    private Random random;
 
    public int ID;
    public static int myID;
    public static int NumOfP;
    public int x;
    public int y;
    
    public boolean alive;		
    public boolean orange;		
    public boolean blue;		
    public boolean green;
    public boolean white;
    public boolean shield;
    public boolean shootingDecision;
    public boolean fireDecision;
            
    private long lastTime = 0;
    private double shieldTime = 0;
    private boolean flagShieldCheck = false;
    
    private int speedAccelerating;
    
    private int speedStopping;
    
    
    private int speedX;
    public int speedY;
    
    private int orangeRadius;
    private int blueRadius;
    private int greenRadius;
    private int whiteRadius;
    public int playerRadius;
    public int fireBallCapacity;
    public int orangeTopSpeed = 12;
    public int blueTopSpeed = 8;
    public int greenTopSpeed = 6;
    public int whiteTopSpeed = 4;
	
	
	
    public int playerTopSpeed;
    public int score;
    public int flagCheck=0;
    
    public Player()
    {
        Initialize();
        LoadContent();
    }
    
    
    private void Initialize()
    {
        random = new Random();
        ResetPlayer();
        
        speedAccelerating = 2;
        speedStopping = 1;
    }
    
    private void LoadContent()
    {
        orangeRadius = 15;
        blueRadius = 18;
        greenRadius = 22;
        whiteRadius = 25;
    }
    
    
    public void ResetPlayer()
    {
        alive = true;
        shield = false;
        shootingDecision = false;
        changeOrange();
        playerRadius = orangeRadius;
        fireBallCapacity = 0;
        score = 0;
        flagCheck = 0;
        x = random.nextInt(Framework.frameWidth - orangeRadius);
        y = random.nextInt(Framework.frameWidth - orangeRadius);
        
        speedX = 0;
        speedY = 0;
    }
    
public void score()
{
    score = score + 10;
    if(score>=20 && score<40 && flagCheck==0)
    {
        flagCheck = 1;      //1 indicates blue state
    }
    else if(score>=40 && score<60 && flagCheck==1)
    {
        flagCheck = 2;      //2 indicates green state
    }
    else if(score>=60 && flagCheck==2)
    {
        flagCheck = 3;      //3 indicates white state
    }
}

public void changeColor(int control)     //control=1 indicates upgrading//0 means downgrade
{
    if(orange && control==1 && flagCheck>0)
        changeBlue();
    else if(blue && control==1 && flagCheck>1)
        changeGreen();
    else if(blue && control==0)
        changeOrange();
    else if(green && control==1 && flagCheck>2)
        changeWhite();
    else if(green && control==0)
        changeBlue();
    else if(white && control==0)
        changeGreen();
}
public void changeOrange()
{
    orange = true;
    blue = false;
    green = false;
    white = false;
    playerRadius = orangeRadius;
    playerTopSpeed = orangeTopSpeed;
    if(speedX>playerTopSpeed)
        speedX = playerTopSpeed;
    if(speedY>playerTopSpeed)
        speedY = playerTopSpeed;
    
}
public void changeBlue()
{
    orange = false;
    blue = true;
    green = false;
    white = false;
    playerRadius = blueRadius;
    playerTopSpeed = blueTopSpeed;
    if(speedX>playerTopSpeed)
        speedX = playerTopSpeed;
    if(speedY>playerTopSpeed)
        speedY = playerTopSpeed;
}
public void changeGreen()
{
    orange = false;
    blue = false;
    green = true;
    white = false;
    playerRadius = greenRadius;
    playerTopSpeed = greenTopSpeed;
    if(speedX>playerTopSpeed)
        speedX = playerTopSpeed;
    if(speedY>playerTopSpeed)
        speedY = playerTopSpeed;
}
public void changeWhite()
{
    orange = false;
    blue = false;
    green = false;
    white = true;
    playerRadius = whiteRadius;
    playerTopSpeed = whiteTopSpeed;
    if(speedX>playerTopSpeed)
        speedX = playerTopSpeed;
    if(speedY>playerTopSpeed)
        speedY = playerTopSpeed;
}

public void UpdateSelf()
{
    if(score<40 && flagCheck==0)
            playerRadius = orangeRadius;
    
    if(Canvas.keyboardKeyState(KeyEvent.VK_W) && speedY>(-1*playerTopSpeed))
        speedY -= speedAccelerating;
    
    if(Canvas.keyboardKeyState(KeyEvent.VK_S) && speedY<playerTopSpeed)
        speedY += speedAccelerating;
    
    if(Canvas.keyboardKeyState(KeyEvent.VK_A) && speedX>(-1*playerTopSpeed))
        speedX -= speedAccelerating;
    
    if(Canvas.keyboardKeyState(KeyEvent.VK_D) && speedX<playerTopSpeed)
        speedX += speedAccelerating;

    if(!Canvas.keyboardKeyState(KeyEvent.VK_W) && !Canvas.keyboardKeyState(KeyEvent.VK_S))
    {
        if(speedY>0)
                speedY -= speedStopping;
        else if(speedY<0)
                speedY += speedStopping;
    }

    if(!Canvas.keyboardKeyState(KeyEvent.VK_A) && !Canvas.keyboardKeyState(KeyEvent.VK_D))
    {
        if(speedX>0)
                speedX -= speedStopping;
        else if(speedX<0)
                speedX += speedStopping;
    }

    if(Canvas.keyboardKeyState(KeyEvent.VK_Q) && ((System.nanoTime()/1000000000)-lastTime)>=2)      //Downgrade
    {
        lastTime = System.nanoTime()/1000000000;
        changeColor(0);
    }

    if(Canvas.keyboardKeyState(KeyEvent.VK_E) && ((System.nanoTime()/1000000000)-lastTime)>=2)      //Upgrade
    {
        lastTime = System.nanoTime()/1000000000;
        changeColor(1);
    }
    
    x += speedX;
    y += speedY;
		
    if(x<14)
        x = 14;
    else if(x>1570)
        x = 1570;

    if(y<14)
            y = 14;
    else if(y>1123)
            y = 1123;
}
    public boolean checkShooting()
    {
        shootingDecision = Canvas.keyboardKeyState(KeyEvent.VK_G);
        return shootingDecision;
    }
    
    public boolean checkFire()
    {
        fireDecision = Canvas.keyboardKeyState(KeyEvent.VK_H);
        return fireDecision;
    }
    
    public void fireBall()
    {
        if((green || white) && fireBallCapacity<3)
            fireBallCapacity++;
    }
    
    public void shield()
    {
       shield = true;
    }
	
    public void UpdatePeerPlayer(String pCharacteristics)
    {
        //pCharacteristics is coded as follows:
        //x,y,score,color(1-orange,2-blue,3-green,4-white)
        if(pCharacteristics==null)
        {
            x = 50;
            y = 50;
            score = 0;
            orange = true;
            playerRadius = orangeRadius;
        }
        else
        {
            try{
                String[] splited = pCharacteristics.split("\\s+");  //space as delimeter
                if(Integer.parseInt(splited[0])==0)
                    alive = false;

                x = Integer.parseInt(splited[1]);
                y = Integer.parseInt(splited[2]);
                int color = Integer.parseInt(splited[3]);
                score = Integer.parseInt(splited[18]);
                fireBallCapacity = Integer.parseInt(splited[20]);
                
                int checkShield = Integer.parseInt(splited[19]);
                if(checkShield==0)
                    shield = false;
                else
                    shield = true;
                if(color==1)
                {
                    orange = true;
                    blue = false;
                    green = false;
                    white = false;
                    playerRadius = orangeRadius;
                }
                else if(color==2)
                {
                    orange = false;
                    blue = true;
                    green = false;
                    white = false;
                    playerRadius = blueRadius;
                }
                else if(color==3)
                {
                    orange = false;
                    blue = false;
                    green = true;
                    white = false;
                    playerRadius = greenRadius;
                }
                else if(color==4)
                {
                    orange = false;
                    blue = false;
                    green = false;
                    white = true;
                    playerRadius = whiteRadius;
                }
            }
            catch(Exception e){}
        }   
    }
	

    public void paintComponent(Graphics2D g2d, int x, int y, int r) 
    {
        super.paintComponent(g2d);
        Ellipse2D.Double circle = new Ellipse2D.Double(x - r, y - r, 2*r, 2*r);
        g2d.fill(circle);
    }
    
    public void Draw(Graphics2D g2d)
    {
        if(alive)
        {
            if(System.nanoTime()/(double)1000000000 - shieldTime >1)
            {
                shieldTime = System.nanoTime()/1000000000;
                if(flagShieldCheck==false)
                    flagShieldCheck = true;
                else
                    flagShieldCheck = false;
            }
            if(shield && flagShieldCheck==true && !white)
            {
                g2d.setColor(Color.CYAN);        //Color of Shield
                if(x<=400 && y<=300)
                    paintComponent(g2d, x, y, playerRadius+6);
                else if(x>=1200-6 && y<=300)
                    paintComponent(g2d, 400+(x-1194)%400, y, playerRadius+6);
                else if(x<=400 && y>=900-30)
                    paintComponent(g2d, x, 300+(y-870)%300, playerRadius+6);
                else if(x>=1200-6 && y>=900-30)
                    paintComponent(g2d, 400+(x-1194)%400, 300+(y-870)%300, playerRadius+6);
                else if(x<=400)
                    paintComponent(g2d, x, 300, playerRadius+6);
                else if(x>=1200-6)
                    paintComponent(g2d, 400+(x-1194)%400, 300, playerRadius+6);
                else if(y<=300)
                    paintComponent(g2d, 400, y, playerRadius+6);
                else if(y>=900-30)
                    paintComponent(g2d, 400, 300+(y-870)%300, playerRadius+6);
                else
                    paintComponent(g2d, 400, 300, playerRadius+6);
            }
            if(orange)
            {
                g2d.setColor(Color.orange);
            }
            else if(blue)
            {
                g2d.setColor(Color.blue);
            }
            else if(green)
            {
                g2d.setColor(Color.green);
            }
            else if(white)
            {
                g2d.setColor(Color.white);
            }
            //Map player start
            if(x<=400 && y<=300)
                paintComponent(g2d, x, y, playerRadius);
            else if(x>=1200-6 && y<=300)
                paintComponent(g2d, 400+(x-1194)%400, y, playerRadius);
            else if(x<=400 && y>=900-30)
                paintComponent(g2d, x, 300+(y-870)%300, playerRadius);
            else if(x>=1200-6 && y>=900-30)
                paintComponent(g2d, 400+(x-1194)%400, 300+(y-870)%300, playerRadius);
            else if(x<=400)
                paintComponent(g2d, x, 300, playerRadius);
            else if(x>=1200-6)
                paintComponent(g2d, 400+(x-1194)%400, 300, playerRadius);
            else if(y<=300)
                paintComponent(g2d, 400, y, playerRadius);
            else if(y>=900-30)
                paintComponent(g2d, 400, 300+(y-870)%300, playerRadius);
            else
                paintComponent(g2d, 400, 300, playerRadius);
            //Map player end

            g2d.setColor(Color.blue);
            if(x<=400 && y<=300)
                g2d.drawString("Player "+x+" "+y, x-5, y+20);
            else if(x>=1200-6 && y<=300)
                g2d.drawString("Player "+x+" "+y, 400+(x-1194)%400-5, y+20);
            else if(x<=400 && y>=900-30)
                g2d.drawString("Player "+x+" "+y, x-5, 300+(y-870)%300+20);
            else if(x>=1200-6 && y>=900-30)
                g2d.drawString("Player "+x+" "+y, 400+(x-1194)%400-5, 300+(y-870)%300+20);
            else if(x<=400)
                g2d.drawString("Player "+x+" "+y, x-5, 300+20);
            else if(x>=1200-6)
                g2d.drawString("Player "+x+" "+y, 400+(x-1194)%400-5, 300+20);
            else if(y<=300)
                g2d.drawString("Player "+x+" "+y, 400-5, y+20);
            else if(y>=900-30)
                g2d.drawString("Player "+x+" "+y, 400-5, 300+(y-870)%300+20);
            else
                g2d.drawString("Player "+x+" "+y, 400-5, 300+20);
            
            g2d.setColor(Color.black);
            g2d.drawString("Player "+this.ID, 5, 15+this.ID*60);  //Modify to Update Points
            g2d.drawString("Score: " + score, 5, 27+this.ID*60);
            g2d.drawString("FireBalls: " + fireBallCapacity + "/3", 5, 39+this.ID*60);
            if(((System.nanoTime()/1000000000)-lastTime)>=2 && flagCheck>0)
                g2d.drawString("UPGRADE READY! ", 5, 51+this.ID*60);
        }
    }
    
    
    public void DrawE(Graphics2D g2d, Player pMac)
    {
        if(alive)
        {
            if(System.nanoTime()/(double)1000000000 - shieldTime >1)
            {
                shieldTime = System.nanoTime()/1000000000;
                if(flagShieldCheck==false)
                    flagShieldCheck = true;
                else
                    flagShieldCheck = false;
            }
            if(shield && flagShieldCheck==true && !white)
            {
                g2d.setColor(Color.CYAN);        //Color of Shield
                if(pMac.x<=400 && pMac.y<=300)
                    paintComponent(g2d, x, y, playerRadius+6);
                else if(pMac.x>=1200-6 && pMac.y<=300)
                    paintComponent(g2d, (x-800+6), y, playerRadius+6);
                else if(pMac.x<=400 && pMac.y>=900-30)
                    paintComponent(g2d, x, y-600+30, playerRadius+6);
                else if(pMac.x>=1200-6 && pMac.y>=900-30)
                    paintComponent(g2d, x-800+6, y-600+30, playerRadius+6);
                else if(pMac.x<=400)
                    paintComponent(g2d, x, 300+y-pMac.y, playerRadius+6);
                else if(pMac.x>=1200-6)
                    paintComponent(g2d, x-800+6, 300+y-pMac.y, playerRadius+6);
                else if(pMac.y<=300)
                    paintComponent(g2d, 400+x-pMac.x, y, playerRadius+6);
                else if(pMac.y>=900-30)
                    paintComponent(g2d, 400+x-pMac.x, y-600+30, playerRadius+6);
                else
                    paintComponent(g2d, 400+x-pMac.x, 300+y-pMac.y, playerRadius+6);
            }
            if(orange)
            {
                g2d.setColor(Color.orange);
            }
            else if(blue)
            {
                g2d.setColor(Color.blue);
            }
            else if(green)
            {
                g2d.setColor(Color.green);
            }
            else if(white)
            {
                g2d.setColor(Color.white);
            }
            //Map player start
            if(pMac.x<=400 && pMac.y<=300)
                paintComponent(g2d, x, y, playerRadius);
            else if(pMac.x>=1200-6 && pMac.y<=300)
                paintComponent(g2d, (x-800+6), y, playerRadius);
            else if(pMac.x<=400 && pMac.y>=900-30)
                paintComponent(g2d, x, y-600+30, playerRadius);
            else if(pMac.x>=1200-6 && pMac.y>=900-30)
                paintComponent(g2d, x-800+6, y-600+30, playerRadius);
            else if(pMac.x<=400)
                paintComponent(g2d, x, 300+y-pMac.y, playerRadius);
            else if(pMac.x>=1200-6)
                paintComponent(g2d, x-800+6, 300+y-pMac.y, playerRadius);
            else if(pMac.y<=300)
                paintComponent(g2d, 400+x-pMac.x, y, playerRadius);
            else if(pMac.y>=900-30)
                paintComponent(g2d, 400+x-pMac.x, y-600+30, playerRadius);
            else
                paintComponent(g2d, 400+x-pMac.x, 300+y-pMac.y, playerRadius);
            //Map player end

            g2d.setColor(Color.blue);
            if(pMac.x<=400 && pMac.y<=300)
                g2d.drawString("Player "+x+" "+y, x-5, y+20);
            else if(pMac.x>=1200-6 && pMac.y<=300)
                g2d.drawString("Player "+x+" "+y, x-800+6-5, y+20);
            else if(pMac.x<=400 && pMac.y>=900-30)
                g2d.drawString("Player "+x+" "+y, x-5, (y-600+30)+20);
            else if(pMac.x>=1200-6 && pMac.y>=900-30)
                g2d.drawString("Player "+x+" "+y, x-800+6-5, (y-600+30)+20);
            else if(pMac.x<=400)
                g2d.drawString("Player "+x+" "+y, x-5, 300+y-pMac.y+20);
            else if(pMac.x>=1200-6)
                g2d.drawString("Player "+x+" "+y, x-800+6-5, 300+y-pMac.y+20);
            else if(pMac.y<=300)
                g2d.drawString("Player "+x+" "+y, 400+x-pMac.x-5, y+20);
            else if(pMac.y>=900-30)
                g2d.drawString("Player "+x+" "+y, 400+x-pMac.x-5, 300+(y-870)%300+20);
            else
                g2d.drawString("Player "+x+" "+y, 400+x-pMac.x-5, 300+y-pMac.y+20);
            
            g2d.setColor(Color.black);
            g2d.drawString("Player "+this.ID, 5, 15+this.ID*60);  //Modify to Update Points
            g2d.drawString("Score: " + score, 5, 27+this.ID*60);
            g2d.drawString("FireBalls: " + fireBallCapacity + "/3", 5, 39+this.ID*60);
            if(((System.nanoTime()/1000000000)-lastTime)>=2 && flagCheck>0)
                g2d.drawString("UPGRADE READY! ", 5, 51+this.ID*60);
        }
    }
}