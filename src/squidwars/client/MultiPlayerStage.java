package squidwars.client;

import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.JFrame;

import squidwars.GameScreen;
import squidwars.host.GameState;


public class MultiPlayerStage extends GameScreen{
	DatagramSocket socket;
	
	public MultiPlayerStage(JFrame parent, DatagramSocket socket, GameState initState, String name, InetAddress serverAddress) {
		super(parent, socket, name, serverAddress);
	}

	@Override
	public void setupWidgets(DatagramSocket socket) {
		add(new StageAnimation(MultiPlayerStage.this, socket, this.clientName, this.serverAddress));
		pack();
	}
	
	@Override
	public void setupWidgets() {
		// TODO Auto-generated method stub
		
	}
}
