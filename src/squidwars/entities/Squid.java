package squidwars.entities;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import squidwars.Chatbox;
import squidwars.host.Constant;

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

    private char direction;
    private Chatbox chatbox;
	private DatagramSocket socket;
//	private int playerNumber;
	

    public Squid(int x, int y, char direction, int counter) {
    	super(x, y);
    	spawnx = x;
    	spawny = y;
    	loadImage(Constant.squidFiles[counter]);
    	this.direction = direction;
    }
    
    public Squid(int x, int y, char direction) {
    	super(x, y);
    	spawnx = x;
    	spawny = y;
    	loadImage(Constant.squidFiles[0]);
    	this.direction = direction;
    }
    
    public void rotateSquid(char dir) {
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