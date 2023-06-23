package squidwars.entities;

import java.io.Serializable;

public class Flag implements Serializable{
	int x;
	int y;
	
	public Flag(int x, int y) {
		this.x=x;
		this.y=y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	public void setX(int x) {
		this.x=x;
	}
	public void setY(int y) {
		this.y=y;
	}
}
