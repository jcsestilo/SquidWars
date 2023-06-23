package squidwars.host;


import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import squidwars.entities.Flag;
import squidwars.entities.Wall;

public class GameState implements Serializable {
	private Map<String, PlayerState> players = new HashMap();
    private List<Wall> walls = new ArrayList<Wall>();
    private Flag flag = new Flag(0,0);
    private char endState = 'X';
    public String mapName;
    
	public GameState() {}
	
	public Map<String, PlayerState> getPlayers() {
		// TODO Auto-generated method stub
		return this.players;
	}
	
	public List<Wall> getWalls(){
		return this.walls;
	}

	public void updatePlayers(String name, PlayerState player) {
		players.put(name,player);
	}
	
	public void addWall(Wall w) {
		walls.add(w);
	}
	
	public void setFlagXY(int x, int y) {
		this.flag.setX(x);
		this.flag.setY(y);
	}
	
	public void setFlag(Flag f) {
		this.flag = f;
	}
	
	public Flag getFlag() {
		return this.flag;
	}
	
	public char getWin() {
		return this.endState;
	}
	
	public void setWin(char winner) {
		this.endState = winner;
		
	}
	
}








