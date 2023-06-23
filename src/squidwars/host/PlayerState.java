package squidwars.host;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.List;
import java.util.ArrayList;
import squidwars.entities.Bullet;

public class PlayerState implements Serializable {
	private InetAddress address;
	private int port;
	private String name;
	private int x,y;
	private int respX;
	private int respY;
	private char direction;
	private List<Bullet> bullets;
	private char team;
	
	public PlayerState(String name,InetAddress address, int port){
		this.address = address;
		this.port = port;
		this.name = name;
		this.bullets = new ArrayList<Bullet>();

		// Temporary
		this.x = 0;
		this.y = 0;
		this.respX = 0;
		this.respY = 0;
		this.direction = 'U';
	}
	
	public String getName() {
		return this.name;
	}

	public InetAddress getAddress() {
		return this.address;
	}

	public int getPort() {
		return this.port;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void setRespX(int x) {
		this.respX = x;
	}
	
	public void setRespY(int y) {
		this.respY = y;
	}
	
	public int getRespX() {
		return this.respX;
	}
	
	public int getRespY() {
		return this.respY;
	}
	
	public char getDirection() {
		return this.direction;
	}
	
	public void setDirection(char dir) {
		this.direction = dir;
	}
	
	public void addBullets(Bullet b) {
		this.bullets.add(b);
	}
	
	public List<Bullet> getBullets(){
		return this.bullets;
	}
	
	public void setTeam(char t) {
		this.team = t;
	}
	
	public char getTeam() {
		return this.team;
	}
}
