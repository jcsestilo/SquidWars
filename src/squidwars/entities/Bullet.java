package squidwars.entities;

import java.io.Serializable;

import squidwars.GameScreen;

public class Bullet implements Serializable {
    private final int MISSILE_SPEED = 60;
    private char direction;
    int x;
    int y;

    public Bullet(int x, int y, char direction) {
        this.direction = direction;
        this.x = x;
        this.y = y;
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
    }
    
    public int getX() {
    	return this.x;
    }
    
    public int getY() {
    	return this.y;
    }
    
    public char getDirection() {
    	return this.direction;
    }
}
