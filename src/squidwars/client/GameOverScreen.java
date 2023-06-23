package squidwars.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramSocket;

import javax.swing.JButton;
import javax.swing.JFrame;

import squidwars.GameScreen;

public class GameOverScreen extends GameScreen {

	public GameOverScreen(JFrame parent, boolean win) {
		super(parent, win);
	}
	
	

	@Override
	public void setupWidgets() {
		String bgsrc = "src/resources/windows/" + (this.status ? "GameOverWin.png" : "GameOverLose.png");
		setBackgroundImage(bgsrc);
		
		JButton btnMainMenu = new JButton("Main Menu");
		btnMainMenu.setBounds(WINDOW_WIDTH/2 - BTN_WIDTH/2, 600, BTN_WIDTH, BTN_HEIGHT);
		btnMainMenu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				goToMainMenu();
			}
		});
		
		add(btnMainMenu);
	}

	@Override
	public void setupWidgets(DatagramSocket socket) {
		// TODO Auto-generated method stub
	}

}
