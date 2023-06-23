package squidwars.entities;

import squidwars.GameScreen;

public class BulletSprite extends Sprite {
    private final int MISSILE_SPEED = 6;
    private char direction;

    public BulletSprite(int x, int y, char direction) {
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
    
    public void rotateBull(char dir) {
    	if(this.direction=='U') {
    		this.rotateImage(0);
    	}
    	else if(this.direction=='D') {
    		this.rotateImage(180);
    	}
    	else if(this.direction=='L') {
    		this.rotateImage(270);
    	}
    	else if(this.direction=='R') {
    		this.rotateImage(90);
    	}
    }

}
