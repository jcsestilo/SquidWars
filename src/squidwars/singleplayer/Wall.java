package squidwars.singleplayer;

public class Wall extends Sprite{
	private boolean destructible;
	
	
	public Wall(int x, int y, boolean destructible) {
		super(x, y);
		this.destructible = destructible;
		loadImage("src/resources/wall.png");  
        getImageDimensions();
	}
	
	public boolean getDestructiblility() {
		return this.destructible;
	}
}
