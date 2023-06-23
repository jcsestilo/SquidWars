package squidwars.client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import squidwars.host.Constant;
import squidwars.host.GameState;
import squidwars.host.Parser;
import squidwars.host.PlayerState;
import squidwars.Chatbox;
import squidwars.entities.*;

public class StageAnimation extends JPanel implements Runnable, ActionListener, Serializable{

	private Chatbox chatbox;
	private Thread cycler;
    public JFrame parent;
    private DatagramSocket socket;
	private InetAddress serverAddress;
    private GameState state;
    private char team;
    
    private Squid squidSprite;
    private WallSprite wallSprite = new WallSprite(0,0);
    private BulletSprite bulletSprite = new BulletSprite(0,0,'U');
    private FlagSprite flagSprite= new FlagSprite(0,0);
    private String name;
    private int bulletCooldown=0;
    
	
    public StageAnimation(JFrame parent, DatagramSocket socket, String name, InetAddress serverAddress) {
    	this.parent = parent;
    	this.socket = socket;
		this.serverAddress = serverAddress;
    	this.state = new GameState();
        setBackground(Color.BLACK);
    	setPreferredSize(new Dimension(1000, 800));
    	setFocusable(true);
    	addKeyListener(new TAdapter());
    	initMap();
    	this.name = name;
    	System.out.println(this.name);
    	this.cycler = new Thread(this);
    	this.chatbox = new Chatbox(socket, serverAddress, name);
    	
    	
    	Thread packetListener = new Thread(new Runnable() {

			@Override
			public void run() {
				while(true) {
					// TODO Auto-generated method stub
					try{
						Thread.sleep((long) 0.05);
					}catch(Exception ioe){}
					
					if(StageAnimation.this.getBullCD()>0) {
						StageAnimation.this.reduceBullCD();
					}
					
					byte[] buf = new byte[12800];
					DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddress, Constant.SERVER_PORT);
					try{
			 			StageAnimation.this.socket.receive(packet);
					}catch(Exception ioe) {}
					
					String serverData = new String(buf).trim();
					
//					System.out.println("received packet in thread anim");
					
					if (serverData.startsWith("GAME STATE")) {
//						System.out.println("received packet in thread anim");
//						System.out.println(serverData);
						StageAnimation.this.state = Parser.parseBuffer(buf);
						
//						repaint();
						
					} else if (serverData.startsWith("CHAT")) {
						String[] chatInfo = serverData.split("<#>");
						System.out.println(chatInfo[1]+" "+chatInfo[2]);
						chatbox.addMsg(chatInfo[1]+" "+chatInfo[2]);
						
					} else if (serverData.startsWith("GAMEOVER")) {
						char winner = serverData.split(":")[1].charAt(0);
						System.out.println("MAY WINNER NA ");
						boolean win = winner == state.getPlayers().get(name).getTeam();
						StageAnimation.this.setVisible(false);
						GameOverScreen gameover = new GameOverScreen(StageAnimation.this.parent, win);
						gameover.setVisible(true);
						
					}
				}
			}
    		
    	});
    	packetListener.start();
    	cycler.start();
    	
    }
	
	private void initMap() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public void paintComponent(Graphics g) {
    	super.paintComponent(g);
		try {
			BufferedImage bg;
			bg = ImageIO.read(new File("src/resources/maps/"+StageAnimation.this.state.mapName+".png"));
			g.drawImage(bg, 0, 0, null);
		} catch (IOException e) {
//			e.printStackTrace();
		}
        draw(g);
    }
	
	private void draw(Graphics g) {
//        if(player.isVisible()) {
//        	g.drawImage(player.imgTransform.filter(player.getImage(), null), player.getX(), player.getY(), this);
//        }
        int counter = 0;
        for(PlayerState p: this.state.getPlayers().values()) {
//        	System.out.println("Dir: "+p.getDirection());
        	this.squidSprite = new Squid(0,0,p.getDirection(),counter);
        	this.squidSprite.rotateSquid(p.getDirection());
        	g.drawImage(squidSprite.imgTransform.filter(squidSprite.getImage(), null), p.getX(), p.getY(), this);
        	counter++;

			// Player name
        	g.setColor(Color.WHITE);
        	g.drawString(p.getName(), p.getX(), p.getY());
        	
        	for(Bullet b: p.getBullets()) {
        		this.bulletSprite = new BulletSprite(0,0, b.getDirection());
        		this.bulletSprite.rotateBull(b.getDirection());
        		g.drawImage(bulletSprite.imgTransform.filter(bulletSprite.getImage(), null), b.getX(), b.getY(), this);
        	}
        }
        
//        if(flag.isVisible()) {
//        	g.drawImage(flag.imgTransform.filter(flag.getImage(), null), flag.getX(), flag.getY(), this);
//        }
        
//        List<Bullet> bullets = player.getBullets();
        
        for (Wall w: this.state.getWalls()) {
        	g.drawImage(wallSprite.imgTransform.filter(wallSprite.getImage(), null), w.getX(), w.getY(), this);
        }
        
//        System.out.println("X: "+this.state.getFlag().getX()+ " Y: "+this.state.getFlag().getY());
        
        g.drawImage(flagSprite.imgTransform.filter(flagSprite.getImage(), null), this.state.getFlag().getX(), this.state.getFlag().getY(), this);
        
        
        Toolkit.getDefaultToolkit().sync();
    }

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			repaint();
		}
	}
	
	private class TAdapter extends KeyAdapter {
		private char last = 'U';
        @Override
        public void keyPressed(KeyEvent e) {
        	if(last!=e.getKeyCode()) {
        		StageAnimation.this.keyPressed(e);
        	}
        	this.last = e.getKeyChar();
        }
        
        @Override
        public void keyReleased(KeyEvent e) {
        	this.last = 'U';
        }
    }
	
	public void sendMovement(String direction, String player){
		String msg = "MOVEMENT:"+player+":"+direction;
		try{
			byte[] buf = msg.getBytes();
			DatagramPacket packet = new DatagramPacket(buf, buf.length, this.serverAddress, 4444);
			socket.send(packet);
		}catch(Exception ex){}
	}
	
	public void reduceBullCD() {
		this.bulletCooldown--;
	}
	
	public int getBullCD() {
		return this.bulletCooldown;
	}
	
	public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
//        if (key == KeyEvent.VK_SPACE) {
//        	shoot();
//        }
        
//        if (!keysPressed.contains(key)) {
//        	keysPressed.add(key);
//        }
        
        // The latest directional key pressed dictates the movement direction of the character
        switch (key) {
	        case KeyEvent.VK_LEFT:
				
				sendMovement("L", this.name);
				
	            break;
	        case KeyEvent.VK_RIGHT:

				sendMovement("R", this.name);
	            break;
	        case KeyEvent.VK_UP:

				sendMovement("U", this.name);
	            break;
	        case KeyEvent.VK_DOWN:

				sendMovement("D", this.name);
	            break;
	        case KeyEvent.VK_ENTER:
	        	if (!chatbox.isVisible()) {
	        		chatbox.setVisible(true);
	        	} else {
	        		chatbox.toFront();
	        		chatbox.repaint();
	        	}
	        	break;
	        case KeyEvent.VK_SPACE:
	        	if(this.bulletCooldown==0) {
	        		sendMovement("S", this.name);
	        		this.bulletCooldown=7;
	        	}
	        	
	        	break;
        }
        
    }


}
