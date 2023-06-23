package squidwars.host;
import java.awt.Rectangle;
import java.util.List;
import java.util.ArrayList;

import squidwars.entities.*;


public class GameCycler {
	private GameState game;
	
	public GameCycler(GameState game) {
		this.game = game;
	}
	
	public void cycleGame() {
		this.moveBullets();
		this.checkCollissions();
	}
	
	public void movePlayer(String name, char move) {
		int dx = 40;
		int dy = 40;
		
		
		PlayerState player = game.getPlayers().get(name);
		boolean withinXAxis = player.getX()+dx >= 0 && player.getX()-dx <= 1000 - 40;;
        boolean withinYAxis = player.getY()+dy >= 0 && player.getY()-dy <= 800 - 2*40; 
		
		
		if(move == 'U' && withinYAxis && checkWallColl(game.getWalls(), 0, -dy, player)) {
			player.setY(player.getY()-dy);
			player.setDirection(move);
		}
		else if(move == 'D' && withinYAxis && checkWallColl(game.getWalls(), 0, dy, player)) {
			player.setY(player.getY()+dy);
			player.setDirection(move);
		}
		else if(move == 'L' && withinXAxis && checkWallColl(game.getWalls(), -dx, 0, player)) {
			player.setX(player.getX()-dx);
			player.setDirection(move);
		}
		else if(move == 'R' && withinXAxis && checkWallColl(game.getWalls(), dx, 0, player)) {
			player.setX(player.getX()+dx);
			player.setDirection(move);
		}
	}
	
	public void playerShoot(String name) {
		PlayerState player = game.getPlayers().get(name);
		
		int bx = player.getX();
    	int by = player.getY();
//    	if (player.getDirection() == 'U') {
//    		by -= 20;
//    	} else if (player.getDirection() == 'D') {
//    		by += 20;
//    	} else if (player.getDirection() == 'R') {
//    		bx += 20/2;
//    	} else if (player.getDirection() == 'L') {
//    		bx -= 20/2;
//    	}
    	player.addBullets(new Bullet(bx, by, player.getDirection()));
	}
	
	public void moveBullets() {
		for(String pname: game.getPlayers().keySet()) {
			PlayerState player = game.getPlayers().get(pname);
			for(Bullet b: player.getBullets()) {
				b.move();
			}
		}
	}
	
	private boolean checkWallColl(List<Wall> walls, int dx, int dy, PlayerState player) {
//		Squid squidSprite = new Squid(player.getX()+dx, player.getY()+dy);
		Rectangle squidRect = new Rectangle(player.getX()+dx, player.getY()+dy, 40, 40);
		
		for(Wall w: walls) {
			WallSprite ws = new WallSprite(w.getX(), w.getY());
			Rectangle wallRect = ws.getBounds();
			if(squidRect.intersects(wallRect)) {
				return false;
			}
		}
		return true;
	}
	
	private void checkCollissions() {
		for(String pname: game.getPlayers().keySet()) {
			PlayerState p = game.getPlayers().get(pname);
			List<Bullet> toRemove = new ArrayList<Bullet>();
			
			for(Bullet b: p.getBullets()) {
				BulletSprite bullSprite = new BulletSprite(b.getX(), b.getY(), 'U');
				Rectangle bullRect = bullSprite.getBounds();
				boolean bulletAlive = true;
				
				for(String pname1: game.getPlayers().keySet()) {
					PlayerState p1 = game.getPlayers().get(pname1);
					Squid playerSprite = new Squid(p1.getX(), p1.getY(), 'U', 1);
					Rectangle playerRect = playerSprite.getBounds();
					if(p1.getTeam() != p.getTeam()) {
						if (bullRect.intersects(playerRect)) {
//							p.getBullets().remove(b);
							toRemove.add(b);
							this.respawnPlayer(p1);
							bulletAlive = false;
							break;
						}
					}
				}
				
				if(!bulletAlive) {
					continue;
				}
				
				for(Wall w: game.getWalls()) {
					WallSprite wallSprite = new WallSprite(w.getX(), w.getY());
					Rectangle wallRect = wallSprite.getBounds();
					if(bullRect.intersects(wallRect)) {
//						p.getBullets().remove(b);
						toRemove.add(b);
					}
				}
			}
			
			for(Bullet b: toRemove) {
				p.getBullets().remove(b);
			}
			
			FlagSprite flagSprite = new FlagSprite(game.getFlag().getX(), game.getFlag().getY());
			Rectangle flagRect = flagSprite.getBounds();
			Squid pSprite = new Squid(p.getX(), p.getY(), 'U');
			Rectangle pRect = pSprite.getBounds();
			
			if(flagRect.intersects(pRect)) {
				game.setWin(p.getTeam());
			}
		}
	}
	
	private void respawnPlayer(PlayerState p) {
		p.setX(p.getRespX());
		p.setY(p.getRespY());
	}
}
