package squidwars.singleplayer;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import squidwars.Chatbox;

import squidwars.Chatbox;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * 
 */
public class Squid extends Sprite {
    private int dx;
    private int dy;
    private int spawnx;
    private int spawny;
    private List<Integer> keysPressed = new ArrayList<Integer>();
    private List<Bullet> bullets;
    private boolean isBulletReloading;
    private Thread bulletReloader;
    private char direction;
    private Chatbox chatbox;
	private DatagramSocket socket;
	

    public Squid(int x, int y, DatagramSocket socket) {
    	super(x, y);
    	spawnx = x;
    	spawny = y;
    	loadImage("src/resources/squid.png");
    	bullets = new ArrayList<>();
    	isBulletReloading = false;
    	reloadBullet();
    	direction = 'U';
      this.socket = socket;
//     	chatbox = new Chatbox(socket);
    }
    
    public void respawn() {
    	x = spawnx;
    	y = spawny;
    }
    
    public void move(List<Wall> walls) {
        boolean withinXAxis = x+dx >= 0 && x+dx <= 1000 - image.getWidth(null);
        boolean withinYAxis = y+dy >= 0 && y+dy <= 800 - 2*image.getHeight(null); 
        if (withinXAxis && !checkWallColl(walls))
        	x += dx;
        if (withinYAxis && !checkWallColl(walls))
        	y += dy;
    }
    
    private boolean checkWallColl(List<Wall> walls) {
    	Rectangle newSquidPos = new Rectangle(x+dx, y+dy, this.width, this.height);
    	
    	for(Wall w: walls) {
    		Rectangle wallPos = w.getBounds();
    		if(wallPos.intersects(newSquidPos)) {
    			return true;
    		}
    	}
    	
    	return false;
    }
    
    public List<Bullet> getBullets() {
    	return bullets;
    }
    
    private void shoot() {
    	if (isBulletReloading && bullets.size() > 0) {
    		return;
    	} else {
    		bulletReloader.stop();
        	reloadBullet();
    	}
    	
    	int bx = x;
    	int by = y;
    	if (direction == 'U') {
    		by -= height;
    	} else if (direction == 'D') {
    		by += height;
    	} else if (direction == 'R') {
    		bx += width/2;
    	} else if (direction == 'L') {
    		bx -= width/2;
    	}
    	bullets.add(new Bullet(bx, by, direction));
    	bulletReloader.start();
    }

	public void setBulletReloading(boolean value) {
    	this.isBulletReloading = value;
    }

	private void reloadBullet() {
    	bulletReloader = new Thread(new Runnable() {
			@Override
			public void run() {
				setBulletReloading(true);
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ex) {					
					setBulletReloading(false);
				} finally {
					setBulletReloading(false);
				}
				
			}
		});
    }
    
    public void updateBullets() {
    	for (int i = 0; i < bullets.size(); i++) {
    		Bullet b = bullets.get(i);
    		
    		if (b.isVisible()) {
    			b.move();
    		} else {
    			bullets.remove(i);
    		}
    	}
    }

	public void sendMovement(String direction, String player){
		String msg = "MOVEMENT:"+player+":"+direction;
		try{
			byte[] buf = msg.getBytes();
			InetAddress address = InetAddress.getByName("localhost");
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4444);
			socket.send(packet);
		}catch(Exception ex){}
	}

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        
        if (key == KeyEvent.VK_SPACE) {
        	shoot();
        }
        
        if (!keysPressed.contains(key)) {
        	keysPressed.add(key);
        }
        
        // The latest directional key pressed dictates the movement direction of the character
        switch (keysPressed.get(keysPressed.size()-1)) {
	        case KeyEvent.VK_LEFT:
	        	direction = 'L';
	            dx = -2;
	            dy = 0;
	            rotateImage(270);
				
				sendMovement("L", "rich");
				
	            break;
	        case KeyEvent.VK_RIGHT:
	            direction = 'R';
	            dx = 2;
	            dy = 0;
	            rotateImage(90);

				sendMovement("R", "rich");
	            break;
	        case KeyEvent.VK_UP:
	            direction = 'U';
	            dy = -2;
	            dx = 0;
	            rotateImage(0);

				sendMovement("U", "rich");
	            break;
	        case KeyEvent.VK_DOWN:
	            direction = 'D';
	            dy = 2;
	            dx = 0;
	            rotateImage(180);

				sendMovement("D", "rich");
	            break;
        }
        
    }

    public void keyReleased(KeyEvent e) {
        
        int key = e.getKeyCode();

        if (keysPressed.contains(key))
        	keysPressed.remove(new Integer(key));
        
        switch (key) {
        	case KeyEvent.VK_LEFT:
                dx = 0;
                break;
            case KeyEvent.VK_RIGHT:
                dx = 0;
                break;
            case KeyEvent.VK_UP:
                dy = 0;
                break;
            case KeyEvent.VK_DOWN:
                dy = 0;
                break;
			case KeyEvent.VK_ENTER:
	        	if (!chatbox.isVisible()) {
	        		chatbox.setVisible(true);
	        	} else {
	        		chatbox.toFront();
	        		chatbox.repaint();
	        	}
	        	break;
        }
    }
}