package squidwars.singleplayer;

import java.net.DatagramSocket;

import javax.swing.JFrame;

import squidwars.GameScreen;


public class SinglePlayerStage extends GameScreen {
	
	DatagramSocket socket;

	public SinglePlayerStage(JFrame parent, DatagramSocket socket) {
		super(parent, socket);
	}

	@Override
	public void setupWidgets(DatagramSocket socket) {
		// TODO Auto-generated method stub
		add(new ThreadAnim(SinglePlayerStage.this, socket));
		pack();

	}
	
	@Override
	public void setupWidgets() {
		// TODO Auto-generated method stub
		
	}
	
}