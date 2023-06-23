package squidwars;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramSocket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import squidwars.client.ClientGameScreen;
import squidwars.host.HostGameScreen;
import squidwars.singleplayer.SinglePlayerStage;
import squidwars.singleplayer.ThreadAnim;


public class StartGame extends GameScreen {
	
	public StartGame(JFrame parent) {
		super(parent);		
	}
	
	@Override
	public void setupWidgets() {
		setBackgroundImage("src/resources/windows/StartWindow.png");
		
		// Window Components
		/*
		JLabel title = new JLabel("START", SwingConstants.CENTER);
		title.setBounds(WINDOW_WIDTH/2 - TITLE_WIDTH/2, 200, TITLE_WIDTH, TITLE_HEIGHT);
		title.setFont(new Font("sans serif", Font.PLAIN, 50));
		*/

		JButton btnHost = createButton("Host", WINDOW_WIDTH/2 - BTN_WIDTH/2, 350, BTN_WIDTH, BTN_HEIGHT);
		btnHost.addActionListener(new StartGameActionListener("Host"));
		JButton btnJoin = createButton("Join", WINDOW_WIDTH/2 - BTN_WIDTH/2, 350+BTN_HEIGHT+15, BTN_WIDTH, BTN_HEIGHT);
		btnJoin.addActionListener(new StartGameActionListener("Join"));
		JButton btnBack = createButton("Go Back", WINDOW_WIDTH/2 - BTN_WIDTH/2, 350+2*BTN_HEIGHT+30+100, BTN_WIDTH, BTN_HEIGHT);
		btnBack.addActionListener(new StartGameActionListener("Go Back"));
		JButton btnSingle = createButton("Play Singleplayer", WINDOW_WIDTH/2 - BTN_WIDTH/2, 350+BTN_HEIGHT+30+50, BTN_WIDTH, BTN_HEIGHT);
		btnSingle.addActionListener(new StartGameActionListener("Start Single"));
		
//		add(title);
		add(btnHost);
		add(btnJoin);
		add(btnBack);
		add(btnSingle);
	}

	
	class StartGameActionListener implements ActionListener {
		private String button;
		
		public StartGameActionListener(String button) {
			this.button = button;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			switch (button) {
				case "Host":
					System.out.println("Host clicked");
					JFrame hostGame = new HostGameScreen(StartGame.this);
					StartGame.this.setVisible(false);
					break;
				case "Join":
					System.out.println("Join clicked");
					new ClientGameScreen(StartGame.this);
					StartGame.this.setVisible(false);
					break;
				case "Start Single":
					System.out.println("Start Singleplayer Game");
//					StartGame.this.setVisible(false);
//					EventQueue.invokeLater(() -> {
//			            JFrame ex = new SinglePlayerStage(StartGame.this);;
//			            ex.setVisible(true);
//			        });
					break;
				case "Go Back":
					System.out.println("Go Back clicked");
					StartGame.this.parent.setVisible(true);
					StartGame.this.setVisible(false);
					StartGame.this.dispose();
					break;
				
				
			}
		}
	}


	@Override
	public void setupWidgets(DatagramSocket socket) {
		// TODO Auto-generated method stub
		
	}
}
