package squidwars.singleplayer;

public class Flag extends Sprite{

	public Flag(int x, int y) {
		super(x, y);
		loadImage("src/resources/flag.png");
		// TODO Auto-generated constructor stub
	}
	public void win() {
		System.out.println("Win");
	}

}
