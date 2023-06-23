package squidwars.singleplayer;

import squidwars.GameScreen;

public class Bullet extends Sprite {
    private final int MISSILE_SPEED = 6;
    private char direction;

    public Bullet(int x, int y, char direction) {
        super(x, y);
        this.direction = direction;
        initMissile();
    }
    
    private void initMissile() {
        
        loadImage("src/resources/bullet.png");  
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
    }

    public void move() {
        
    	if (direction == 'U') {
    		y -= MISSILE_SPEED;
    	} else if (direction == 'D') {
    		y += MISSILE_SPEED;
    	} else if (direction == 'L') {
    		x -= MISSILE_SPEED;
    	} else if (direction == 'R') {
    		x += MISSILE_SPEED;
    	}
        
        if (x > GameScreen.WINDOW_WIDTH || x < 0 || y > GameScreen.WINDOW_HEIGHT || y < 0) {
            visible = false;
        }
    }
}
