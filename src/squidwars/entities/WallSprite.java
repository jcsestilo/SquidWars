package squidwars.entities;

public class WallSprite extends Sprite{
	public WallSprite(int x, int y) {
		super(x,y);
		loadImage("src/resources/wall.png");  
        getImageDimensions();
	}
}
