package squidwars.singleplayer;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EnemySquid extends Sprite{
	private char direction;
	private int moveDistance;
	private int dy;
	private int dx;
	private int spawnx;
	private int spawny;
	public List<Bullet> bullets;
	private boolean isBulletReloading;
	private Thread bulletReloader;
	
	public EnemySquid (int x, int y, char direction){
		super(x,y);
		this.direction = direction;
		this.spawnx = x;
		this.spawny = y;
		initSquid();
	}
	
	public void respawn() {
		x = spawnx;
		y = spawny;
	}
	
	private void initSquid() {
        loadImage("src/resources/enemysquid.png");  
        getImageDimensions();
        
        int rotation = 0; // if direction == 'U'
        if (direction == 'R') {
        	rotation = 90;
        } else if (direction == 'D') {
        	rotation = 180;
        } else if (direction == 'L') {
        	rotation = 270;
        }
        rotateImage(rotation);
        
        this.moveDistance = 10;
        this.isBulletReloading = true;
        this.bullets = new ArrayList<>();
        reloadBullet();
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
    	bullets.add(new Bullet(bx, by, this.direction));
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
    
	private void reverseDirection() {
		if(this.direction=='U') {
			this.direction ='D';
			this.dx=0;
			this.dy=2;
			rotateImage(180);
		}
		else if(this.direction=='D') {
			this.direction ='U';
			this.dx=0;
			this.dy=-2;
			rotateImage(0);
		}
		else if(this.direction=='L') {
			this.direction ='R';
			this.dx=2;
			this.dy=0;
			rotateImage(90);
		}
		else if(this.direction=='R') {
			this.direction ='L';
			this.dx=-2;
			this.dy=0;
			rotateImage(270);
		}
	}
	
	public void doAction(List<Wall> walls) {
		shoot();
		if(this.direction=='U') {
			this.dx=0;
			this.dy=-2;
			rotateImage(0);
		}
		else if(this.direction=='D') {
			this.dx=0;
			this.dy=2;
			rotateImage(180);
		}
		else if(this.direction=='L') {
			this.dx=-2;
			this.dy=0;
			rotateImage(270);
		}
		else if(this.direction=='R') {
			this.dx=2;
			this.dy=0;
			rotateImage(90);
		}
		
		if(checkWallColl(walls))
			this.reverseDirection();
		
		boolean withinXAxis = x+dx >= 0 && x+dx <= 1000 - image.getWidth(null);
        boolean withinYAxis = y+dy >= 0 && y+dy <= 800 - 2*image.getHeight(null);
        if (withinXAxis)
        	x += dx;
      
        if (withinYAxis)
        	y += dy;

        this.moveDistance -= 2;
        if(this.moveDistance==0) {
        	Random r = new Random();
        	int[] dist = {10,20,30,40,50,60,70,80,90,100};
        	this.moveDistance= dist[r.nextInt(10)];
        	char[] d = {'U','D','R','L'};
        	this.direction = d[r.nextInt(4)];
        }
        
	}

}
