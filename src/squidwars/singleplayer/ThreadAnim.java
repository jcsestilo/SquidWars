package squidwars.singleplayer;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import squidwars.client.ClientWaitingScreen;
import squidwars.host.GameState;
import squidwars.host.Parser;
import squidwars.host.PlayerState;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Scanner;

public class ThreadAnim extends JPanel implements ActionListener, Runnable {
	private Thread animator;
    public JFrame parent;
    private DatagramSocket socket;
    private GameState state;
    
    private Squid player;
    private List<EnemySquid> enemies;
    private List<Wall> walls;
    private Flag flag;
	
    public ThreadAnim(JFrame parent, DatagramSocket socket) {
    	this.parent = parent;
    	this.socket = socket;
    	this.state = new GameState();
        setBackground(Color.BLACK);
    	setPreferredSize(new Dimension(1000, 800));
    	setFocusable(true);
    	addKeyListener(new TAdapter());
    	initMap();
    	
    	Thread packetListener = new Thread(new Runnable() {

			@Override
			public void run() {
				while(true) {
					// TODO Auto-generated method stub
					try{
						Thread.sleep(1);
					}catch(Exception ioe){}
					
					byte[] buf = new byte[6400];
					DatagramPacket packet = new DatagramPacket(buf, buf.length);
					try{
			 			ThreadAnim.this.socket.receive(packet);
					}catch(Exception ioe) {}
					
					String serverData = new String(buf).trim();
					
//					System.out.println("received packet in thread anim");
					
					if (serverData.startsWith("GAME STATE")) {
//						System.out.println("received packet in thread anim");
						System.out.println(serverData);
						ThreadAnim.this.state = Parser.parseBuffer(buf);
					} else if (serverData.startsWith("CHAT")) {
						
					}
				}
			}
    		
    	});
    	packetListener.start();
    }
    

    private void initMap() {
    	walls = new ArrayList<>();
    	enemies = new ArrayList<>();

    	try {
	    	File map = new File("src/maps/classic.map");
	    	Scanner mapper = new Scanner(map);
	    	for(int i=0; i<20; i++) {
	    		String row = mapper.nextLine();
	    		char[] row1 = row.toCharArray();
	    		for(int j=0; j<25; j++) {
		    		if(row1[j]=='W') {
		    			walls.add(new Wall(j*40, i*40, false));
		    		}
		    		if(row1[j]=='P') {
		    			player = new Squid(j*40,i*40, socket);
		    		}
		    		if(row1[j]=='E') {
		    			enemies.add(new EnemySquid(j*40,i*40,'U'));
		    		}
		    		if(row1[j]=='F') {
		    			flag = new Flag(j*40, i*40);
		    		}
		    	}
	    	}
	    	mapper.close();
    	} catch (FileNotFoundException e) {
    		System.out.println("An error occurred.");
    	}
    }
    
    public void updateEnemies() {
    	for (int i = 0; i < enemies.size(); i++) {

            EnemySquid e = enemies.get(i);
            
            if (!e.isVisible()) {
            	enemies.remove(i);
            }
        }
	}
    
    public void updateWalls() {
    	for (int i = 0; i < walls.size(); i++) {

            Wall w = walls.get(i);
            
            if (!w.isVisible()) {
            	walls.remove(i);
            }
        }
	}
    
    public void checkCollisions() {
    	Rectangle flagRectangle = flag.getBounds();
    	Rectangle playerRectangle = player.getBounds();
    	
    	if(flagRectangle.intersects(playerRectangle)) {
    		System.out.println("Win");
    		flag.setVisible(false);
    	}
    	
    	for(Bullet bullet: player.getBullets()) {
    		Rectangle b = bullet.getBounds();
    		for(EnemySquid enemy:enemies) {
        		Rectangle e = enemy.getBounds();
        		if(b.intersects(e)) {
        			bullet.setVisible(false);
//        			enemy.setVisible(false);
        			enemy.respawn();
        		}
        	}
    		
    		for(Wall wall:walls) {
        		Rectangle w = wall.getBounds();
        		if(b.intersects(w)) {
        			bullet.setVisible(false);
        			if(wall.getDestructiblility()) {
        				wall.setVisible(false);
        			}
        		}
        	}
    	}
    	
    	for(EnemySquid enemy: enemies) {
    		for(Bullet eb: enemy.bullets) {
    			Rectangle s = this.player.getBounds();
    			Rectangle b = eb.getBounds();
    			if(s.intersects(b)) {
//    				player.setVisible(false);
    				eb.setVisible(false);
    				System.out.println("respawn");
    				player.respawn();
    			}
    			for(Wall wall:walls) {
            		Rectangle w = wall.getBounds();
            		if(b.intersects(w)) {
            			eb.setVisible(false);
            			if(wall.getDestructiblility()) {
            				wall.setVisible(false);
            			}
            		}
            	}
    		}
    	}
    }

    @Override
    public void addNotify() {
        super.addNotify();

        animator = new Thread(this);
        animator.start();
    }
    
    public void paintComponent(Graphics g) {
    	super.paintComponent(g);
        draw(g);
    }
    
    private void draw(Graphics g) {
        if(player.isVisible()) {
        	g.drawImage(player.imgTransform.filter(player.getImage(), null), player.getX(), player.getY(), this);
        }
        
        if(flag.isVisible()) {
        	g.drawImage(flag.imgTransform.filter(flag.getImage(), null), flag.getX(), flag.getY(), this);
        }
        
        List<Bullet> bullets = player.getBullets();
        
        for (Bullet b: bullets) {
        	g.drawImage(b.imgTransform.filter(b.getImage(), null), b.getX(), b.getY(), this);
        }
        
        for (EnemySquid e: enemies) {
        	g.drawImage(e.imgTransform.filter(e.getImage(), null), e.getX(), e.getY(), this);
        	for(Bullet b: e.bullets) {
        		g.drawImage(b.imgTransform.filter(b.getImage(), null), b.getX(), b.getY(), this);
        	}
        }
        
        for (Wall w: walls) {
        	g.drawImage(w.imgTransform.filter(w.getImage(), null), w.getX(), w.getY(), this);
        }
        
        Toolkit.getDefaultToolkit().sync();
    }
    
    void cycle() {
    	// Game State
    	for (Iterator ite = this.state.getPlayers().keySet().iterator(); ite.hasNext();) {
    		String name = (String)ite.next();
			PlayerState player = (PlayerState)this.state.getPlayers().get(name);
    	}
    	
    	// Player Movement
    	
    	player.move(walls);
    	player.updateBullets();
    	updateEnemies();
    	updateWalls();
    	checkCollisions();
    	for(EnemySquid e: this.enemies) {
    		e.doAction(walls);
    		e.updateBullets();
    	}
    	repaint();
    }
    
    
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
		long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (true) {

        	cycle();
        	
            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = 16 - timeDiff;

            if (sleep < 0) {
                sleep = 16;
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException e) {

                String msg = String.format("Thread interrupted: %s", e.getMessage());

                JOptionPane.showMessageDialog(this, msg, "Error",
                    JOptionPane.ERROR_MESSAGE);
            }

            beforeTime = System.currentTimeMillis();
        }
	}
	
	/**
	 * Key press listeners
	 *
	 */
	private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);
        }
    }

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}