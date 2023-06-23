package squidwars.entities;
import java.io.Serializable;

public class Wall implements Serializable{
	private boolean destructible;
	int x;
	int y;
	
	
	public Wall(int x, int y, boolean destructible) {
//		super(x, y);
		this.x=x;
		this.y=y;
		this.destructible = destructible;
	}
	
	public boolean getDestructiblility() {
		return this.destructible;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
}
