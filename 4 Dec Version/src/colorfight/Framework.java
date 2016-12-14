package colorfight;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * @author Nisarg Tike
 */

public class Framework extends Canvas {
    
    private int pause = 0;
    
    public static String machineIP;
    
    public static int frameWidth;
    public static int createGameWidth;
    public static int joinGameWidth;
    public static int controlWidth;
    public static int controlsWidth;
    public static int instructionsWidth;
    public static int colorFightLogoWidth;
    public static int instructionsLogoWidth;
    public static int controlsLogoWidth;
    public static int backWidth;
    
    public static int numOfPlayers;
    public static int join;   //0 indicates join//1 indicates create a game
    
    public static int frameHeight;
    public static int createGameHeight;
    public static int joinGameHeight;
    public static int controlHeight;
    public static int controlsHeight;
    public static int instructionsHeight;
    public static int controlsLogoHeight;
    public static int colorFightLogoHeight;
    public static int instructionsLogoHeight;
    
    public static String serverName;
    private int checkDialog = 0;
    private JLabel headerLabel;
    private JLabel statusLabel;
    private JPanel controlPanel;
    private JFrame mainFrame;
    
    public static int backHeight;
    private int flag=0;
    private int flagMouse = 0;
    private int menuFlag = 0;
    private int xcor=0;
    private int ycor=0;

    public static final long secInNanosec = 1000000000L;
    
    
    public static final long milisecInNanosec = 1000000L;
    
   
    private final int GAME_FPS = 16;
    
    private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;
    
    public static enum GameState{STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED}
    
    
    public static GameState gameState;
    
    
    private long gameTime;
    
    private long lastTime;
    
    private Game game;
    
    
    
    private BufferedImage colorFightMenuImg;
    private BufferedImage colorFightLogoImg;
    private BufferedImage instructionsLogoImg;
    private BufferedImage controlsImg;
    
    private BufferedImage createGameImg;
    private BufferedImage createGameActiveImg;
    private BufferedImage joinGameImg;
    private BufferedImage joinGameActiveImg;
    private BufferedImage controlImg;
    private BufferedImage controlsLogoImg;
    private BufferedImage controlActiveImg;
    private BufferedImage instructionsImg;
    private BufferedImage instructionsActiveImg;
    private BufferedImage backImg;
    private BufferedImage backActiveImg;
    
    private int createGameXloc=45;
    private int createGameYloc=100;
    private int joinGameXloc=45;
    private int joinGameYloc=180;
    private int backXloc=500;
    private int backYloc=25;
    private int controlXloc=45;
    private int controlYloc=340;    
    private int instructionsXloc=45;
    private int instructionsYloc=260;
    private int colorFightXloc;
    private int colorFightYloc;
    private int controlsLogoXloc;
    private int controlsLogoYloc;
    private int instructionsLogoXloc;
    private int instructionsLogoYloc;
    
    Sound bgm = new Sound();
    JTextField player1Name;
    JTextField player2Name;
    public Framework ()
    {
        super();
        
        gameState = GameState.VISUALIZING;
        
        Thread gameThread = new Thread() {
            @Override
            public void run(){
                GameLoop();
            }
        };
        gameThread.start();
    }
    
    private void Initialize()
    {
        
    }
    
   
    private void LoadContent()
    {
        try
        {
            backImg = ImageIO.read(getClass().getResource("/images1/back.png"));
            colorFightMenuImg = ImageIO.read(getClass().getResource("/images1/title.jpg"));
            createGameImg = ImageIO.read(getClass().getResource("/images1/createGame1.png"));
            createGameActiveImg = ImageIO.read(getClass().getResource("/images1/createGame1_active.png"));
            joinGameImg = ImageIO.read(getClass().getResource("/images1/join1.png"));
            joinGameActiveImg = ImageIO.read(getClass().getResource("/images1/join1_active.png"));
            instructionsImg = ImageIO.read(getClass().getResource("/images1/instructions.png"));
            instructionsActiveImg = ImageIO.read(getClass().getResource("/images1/instructions_active.png"));
            controlImg = ImageIO.read(getClass().getResource("/images1/control.png"));
            controlActiveImg = ImageIO.read(getClass().getResource("/images1/control_active.png"));
            colorFightLogoImg = ImageIO.read(getClass().getResource("/images1/logo.png"));
            instructionsLogoImg = ImageIO.read(getClass().getResource("/images1/instructionsLogo.png"));
            controlsLogoImg = ImageIO.read(getClass().getResource("/images1/controlsLogo.png"));
            controlsImg = ImageIO.read(getClass().getResource("/images1/controls.jpg"));
            createGameWidth = createGameImg.getWidth();
            createGameHeight = createGameImg.getHeight();
            joinGameWidth = joinGameImg.getWidth();
            joinGameHeight = joinGameImg.getHeight();
            controlWidth = controlImg.getWidth();
            controlHeight = controlImg.getHeight();
            instructionsWidth = instructionsImg.getWidth();
            instructionsHeight = instructionsImg.getHeight();
            colorFightLogoWidth = colorFightLogoImg.getWidth();
            colorFightLogoHeight = colorFightLogoImg.getHeight();
            controlsWidth = controlsImg.getWidth();
            controlsHeight = controlsImg.getHeight();
            backWidth = backImg.getWidth();
            backHeight = backImg.getHeight();
            controlsLogoWidth = controlsLogoImg.getWidth();
            controlsLogoHeight = controlsLogoImg.getHeight();
            instructionsLogoWidth = instructionsLogoImg.getWidth();
            instructionsLogoHeight = instructionsLogoImg.getHeight();
            createGameActiveImg = ImageIO.read(getClass().getResource("/images1/createGame1_active.png"));
            backActiveImg = ImageIO.read(getClass().getResource("/images1/back_active.png"));
        }
        catch (Exception ex) {
            Logger.getLogger(Framework.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
    private void prepareGUI()
    {
      mainFrame = new JFrame("ServerName");
      mainFrame.setSize(350,150);
      mainFrame.setLayout(new GridLayout(3, 1));
      mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
            checkDialog = 0;
         }        
      });
      headerLabel = new JLabel("", JLabel.CENTER);        
      statusLabel = new JLabel("",JLabel.CENTER);    

      statusLabel.setSize(350,100);

      controlPanel = new JPanel();
      controlPanel.setLayout(new FlowLayout());

      mainFrame.add(headerLabel);
      mainFrame.add(controlPanel);
      mainFrame.add(statusLabel);
      mainFrame.setVisible(true);
   }

   private void showTextFieldDemo()
   {
      headerLabel.setText("Enter ServerName"); 

      JLabel  namelabel= new JLabel("ServerName: ", JLabel.RIGHT);
      final JTextField userText = new JTextField(6);

      JButton loginButton = new JButton("Join!");
      loginButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {     
            serverName = userText.getText(); 
            statusLabel.setText(serverName);
            mainFrame.dispose();
            newGame();
         }
      }); 

      controlPanel.add(namelabel);
      controlPanel.add(userText);
      controlPanel.add(loginButton);
      mainFrame.setLocation(2*Framework.frameWidth/3, Framework.frameHeight/2);
      mainFrame.setVisible(true);
   }
    
   
   
   private String getMachineIP()
   {
       InetAddress ip;
       String ipAddr="";
	  try {
		ip = InetAddress.getLocalHost();
		ipAddr = ip.getHostAddress();
	  } catch (UnknownHostException e) {}
          machineIP = ipAddr;
          return ipAddr;
   }
   private void prepareGUImaster()
    {
      mainFrame = new JFrame("ServerName");
      mainFrame.setSize(350,150);
      mainFrame.setLayout(new GridLayout(3, 1));
      mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
            checkDialog = 0;
         }        
      });
      headerLabel = new JLabel("", JLabel.CENTER);        
      statusLabel = new JLabel("",JLabel.CENTER);    

      statusLabel.setSize(350,100);

      controlPanel = new JPanel();
      controlPanel.setLayout(new FlowLayout());

      mainFrame.add(headerLabel);
      mainFrame.add(controlPanel);
      mainFrame.add(statusLabel);
      mainFrame.setVisible(true);
   }

   private void showTextFieldDemomaster()
   {
      headerLabel.setText("YOUR IP: "+getMachineIP()); 

      JLabel  namelabel= new JLabel("No of Players: ", JLabel.RIGHT);
      final JTextField userText = new JTextField(6);

      JButton loginButton = new JButton("Create!");
      loginButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            numOfPlayers = Integer.parseInt(userText.getText()); 
            statusLabel.setText(Integer.toString(numOfPlayers));
            mainFrame.dispose();
            newGame();
         }
      });
      
      controlPanel.add(namelabel);
      controlPanel.add(userText);
      controlPanel.add(loginButton);
      mainFrame.setLocation(2*Framework.frameWidth/3, Framework.frameHeight/2);
      mainFrame.setVisible(true);
   }
   
    private void GameLoop()
    {
       
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();
        
       
        long beginTime, timeTaken, timeLeft;
        
        while(true)
        {
            beginTime = System.nanoTime();
            
            switch (gameState)
            {
                case PLAYING:
                    if(pause==0)
                    {
                        gameTime += System.nanoTime() - lastTime;
                    
                        game.UpdateGame(gameTime, mousePosition());
                                       
                        lastTime = System.nanoTime();
                    }
                    
                break;
                case GAMEOVER:
                    //...
                break;
                case MAIN_MENU:
                    if(flag==0)
                    {
                        bgm.playSound("ncs_fade.wav");
                    }
                    flag = 1;
                    if(flag==2)
                    {
                        
                        flag = 1;
                    }
                break
                        ;
                case OPTIONS:
                break;
                
                case GAME_CONTENT_LOADING:
                break;
                
                case STARTING:
                    Initialize();
                    LoadContent();
                    gameState = GameState.MAIN_MENU;
                break;
                
                case VISUALIZING:
                    
                    if(this.getWidth() > 1 && visualizingTime > secInNanosec)
                    {
                        frameWidth = this.getWidth();
                        frameHeight = this.getHeight();
                        
                        gameState = GameState.STARTING;
                    }
                    else
                    {
                        visualizingTime += System.nanoTime() - lastVisualizingTime;
                        lastVisualizingTime = System.nanoTime();
                    }
                break;
            }
            
            repaint();
            
            timeTaken = System.nanoTime() - beginTime;
            timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec;
            if (timeLeft < 10) 
                timeLeft = 10;
            try {
                 Thread.sleep(timeLeft);
            } catch (InterruptedException ex) { }
        }
    }
    
    @Override
    public void Draw(Graphics2D g2d)
    {
        switch (gameState)
        {
            case PLAYING:
                game.Draw(g2d, mousePosition());
                if(pause==1)
                    g2d.drawString("GAME PAUSED", 400, 300);
            break;
            case GAMEOVER:
                game.DrawGameOver(g2d, mousePosition(), gameTime);
            break;
            case MAIN_MENU:
                g2d.drawImage(colorFightMenuImg, 0, 0, frameWidth, frameHeight, null);
                
                if(menuFlag==0)
                {
                    g2d.drawImage(colorFightLogoImg, colorFightXloc, colorFightYloc, colorFightLogoWidth, colorFightLogoHeight, null);
                    if(flagMouse==0)
                    {
                        g2d.drawImage(createGameImg, createGameXloc, createGameYloc, createGameWidth, createGameHeight, null);
                        g2d.drawImage(joinGameImg, joinGameXloc, joinGameYloc, joinGameWidth, joinGameHeight, null);
                        g2d.drawImage(instructionsImg, instructionsXloc, instructionsYloc, instructionsWidth, instructionsHeight, null);
                        g2d.drawImage(controlImg, controlXloc, controlYloc, controlWidth, controlHeight, null);
                    } 
                    else if(flagMouse==1)
                    {
                        g2d.drawImage(createGameActiveImg, createGameXloc, createGameYloc, createGameWidth, createGameHeight, null);
                        g2d.drawImage(joinGameImg, joinGameXloc, joinGameYloc, joinGameWidth, joinGameHeight, null);
                        g2d.drawImage(instructionsImg, instructionsXloc, instructionsYloc, instructionsWidth, instructionsHeight, null);
                        g2d.drawImage(controlImg, controlXloc, controlYloc, controlWidth, controlHeight, null);
                    }

                    else if(flagMouse==2)
                    {
                        g2d.drawImage(createGameImg, createGameXloc, createGameYloc, createGameWidth, createGameHeight, null);
                        g2d.drawImage(joinGameImg, joinGameXloc, joinGameYloc, joinGameWidth, joinGameHeight, null);
                        g2d.drawImage(instructionsActiveImg, instructionsXloc, instructionsYloc, instructionsWidth, instructionsHeight, null);    
                        g2d.drawImage(controlImg, controlXloc, controlYloc, controlWidth, controlHeight, null);
                    }
                    else if(flagMouse==3)
                    {
                        g2d.drawImage(createGameImg, createGameXloc, createGameYloc, createGameWidth, createGameHeight, null);
                        g2d.drawImage(joinGameImg, joinGameXloc, joinGameYloc, joinGameWidth, joinGameHeight, null);
                        g2d.drawImage(instructionsImg, instructionsXloc, instructionsYloc, instructionsWidth, instructionsHeight, null);
                        g2d.drawImage(controlActiveImg, controlXloc, controlYloc, controlWidth, controlHeight, null);
                    }
                    else if(flagMouse==6)
                    {
                        g2d.drawImage(createGameImg, createGameXloc, createGameYloc, createGameWidth, createGameHeight, null);
                        g2d.drawImage(joinGameActiveImg, joinGameXloc, joinGameYloc, joinGameWidth, joinGameHeight, null);
                        g2d.drawImage(instructionsImg, instructionsXloc, instructionsYloc, instructionsWidth, instructionsHeight, null);
                        g2d.drawImage(controlImg, controlXloc, controlYloc, controlWidth, controlHeight, null);
                    }
                }
                else if(menuFlag==1)
                {
                    if(flagMouse==4)
                        g2d.drawImage(backActiveImg, backXloc, backYloc, backWidth, backHeight, null);
                    else 
                        g2d.drawImage(backImg, backXloc, backYloc, backWidth, backHeight, null);
                    g2d.drawImage(instructionsLogoImg, instructionsLogoXloc, instructionsLogoYloc, instructionsLogoWidth, instructionsLogoHeight, null);
                }
                else if(menuFlag==2)
                {
                    g2d.drawImage(controlsImg, 0, 0, controlsWidth, controlsHeight, null);
                    if(flagMouse==4)
                        g2d.drawImage(backActiveImg, backXloc, backYloc, backWidth, backHeight, null);
                    else 
                        g2d.drawImage(backImg, backXloc, backYloc, backWidth, backHeight, null);
                    g2d.drawImage(controlsLogoImg, controlsLogoXloc, controlsLogoYloc, controlsLogoWidth, controlsLogoHeight, null); 
                }
                
            break;
            
            case OPTIONS:
            break;
            
            case GAME_CONTENT_LOADING:
                g2d.setColor(Color.blue);
                g2d.drawString("GAME is LOADING", frameWidth / 2 - 50, frameHeight / 2);
            break;
        }
    }
    
    private void newGame()
    {
        gameTime = 0;
        lastTime = System.nanoTime();
        
        game = new Game();
    }
    
    private void restartGame()
    {
        gameTime = 0;
        lastTime = System.nanoTime();
        
        game.RestartGame();
        gameState = GameState.PLAYING;
    }
    
    private Point mousePosition()
    {
        try
        {
            Point mp = this.getMousePosition();
            
            if(mp != null)
                return this.getMousePosition();
            else
                return new Point(0, 0);
        }
        catch (Exception e)
        {
            return new Point(0, 0);
        }
    }
    
    @Override
    public void keyReleasedFramework(KeyEvent e)
    {
        switch (gameState)
        {
            case PLAYING:
                if(e.getKeyCode() == KeyEvent.VK_P)
                {
                    if(pause==1)
                        pause = 0;
                    else
                        pause = 1;
                }
                break;
                 
            case GAMEOVER:
                if(e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_ENTER)
                    restartGame();
            break;
        }
    }
    
    @Override
    public void mouseClicked(MouseEvent e)
    {
        switch(gameState)
        {
            case MAIN_MENU:
                if(e.getX()>createGameXloc && e.getX()<createGameXloc+createGameWidth && e.getY()>createGameYloc && e.getY()<createGameYloc+createGameHeight && checkDialog==0)
                {
                    join = 1;
                    checkDialog = 1;
                    prepareGUImaster();
                    showTextFieldDemomaster();
                }
                else if(e.getX()>joinGameXloc && e.getX()<joinGameXloc+joinGameWidth && e.getY()>joinGameYloc && e.getY()<joinGameYloc+joinGameHeight && checkDialog==0)
                {
                    join = 0;
                    checkDialog = 1;
                    prepareGUI();
                    showTextFieldDemo();
                }
                else if(e.getX()>instructionsXloc && e.getX()<instructionsXloc+ instructionsWidth && e.getY()>instructionsYloc && e.getY()<instructionsYloc+instructionsHeight)
                    menuFlag = 1;
                else if(e.getX()>controlXloc && e.getX()<controlXloc+ controlWidth && e.getY()>controlYloc && e.getY()<controlYloc+controlHeight)
                    menuFlag = 2;
                else if(e.getPoint().distance(backXloc + backWidth/2, backYloc + backHeight/2)<backWidth/2)
                    menuFlag = 0;
                
            break;
        }
    }
    @Override
    public void mouseMoved(MouseEvent e)
    {
        xcor = e.getX();
        ycor = e.getY();
        if(e.getX()>createGameXloc && e.getX()<createGameXloc+createGameWidth && e.getY()>createGameYloc && e.getY()<createGameYloc+createGameHeight)
            flagMouse = 1;
        else if(e.getX()>joinGameXloc && e.getX()<joinGameXloc+joinGameWidth && e.getY()>joinGameYloc && e.getY()<joinGameYloc+joinGameHeight)
            flagMouse = 6;
        else if(e.getX()>instructionsXloc && e.getX()<instructionsXloc+ instructionsWidth && e.getY()>instructionsYloc && e.getY()<instructionsYloc+instructionsHeight)
            flagMouse = 2;
        else if(e.getX()>controlXloc && e.getX()<controlXloc+ controlWidth && e.getY()>controlYloc && e.getY()<controlYloc+controlHeight)
            flagMouse = 3;
        else if(e.getPoint().distance(backXloc + backWidth/2, backYloc + backHeight/2)<backWidth/2 && menuFlag!=0)
            flagMouse = 4;
        else if(e.getX()>joinGameXloc && e.getX()<joinGameXloc+joinGameWidth && e.getY()>joinGameYloc && e.getY()<joinGameYloc+joinGameHeight)
            flagMouse = 5;
        else
            flagMouse = 0;
    }
}