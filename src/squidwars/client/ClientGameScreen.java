package squidwars.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import squidwars.GameScreen;
import squidwars.host.Constant;


public class ClientGameScreen extends GameScreen {
	private JTextField txtUsername;
	private JTextField txtServerName;
	
	public ClientGameScreen(JFrame parent) {
		super(parent);
		setTitle("Squid Wars - Join a game");
	}
	
	@Override
	public void setupWidgets() {
		setBackgroundImage("src/resources/windows/JoinWindow.png");
		
		// Window Components
		/*
		JLabel title = new JLabel("JOIN", SwingConstants.CENTER);
		title.setBounds(WINDOW_WIDTH/2 - TITLE_WIDTH/2, 75, TITLE_WIDTH, TITLE_HEIGHT);
 		title.setFont(new Font("sans serif", Font.PLAIN, 30));
		*/
		
 		// Username
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(WINDOW_WIDTH/2 - TEXTFIELD_WIDTH/2, 310, 100, 30);
 		lblUsername.setFont(new Font("sans serif", Font.BOLD, 12));
 		lblUsername.setForeground(Color.WHITE);
		txtUsername = new JTextField();
		txtUsername.setBounds(WINDOW_WIDTH/2 - TEXTFIELD_WIDTH/2, 310+30, TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT);
		
		// Server
		JLabel lblServerName = new JLabel("Server Name");
		lblServerName.setBounds(WINDOW_WIDTH/2 - TEXTFIELD_WIDTH/2, 395, 100, 30);
 		lblServerName.setFont(new Font("sans serif", Font.BOLD, 12));
 		lblServerName.setForeground(Color.WHITE);
		txtServerName = new JTextField();
		txtServerName.setBounds(WINDOW_WIDTH/2 - TEXTFIELD_WIDTH/2, 395+30, TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT);
		
		
		JButton btnCreateServer = createButton("Join Game", WINDOW_WIDTH/2 - BTN_WIDTH/2, 500, BTN_WIDTH, BTN_HEIGHT);
		btnCreateServer.addActionListener(new StartGameActionListener("Join Game"));
		JButton btnBack = createButton("Go Back", WINDOW_WIDTH/2 - BTN_WIDTH/2, 500+BTN_HEIGHT+15, BTN_WIDTH, BTN_HEIGHT);
		btnBack.addActionListener(new StartGameActionListener("Go Back"));

//		add(title);
		add(lblUsername);
		add(txtUsername);
		add(lblServerName);
		add(txtServerName);
		add(btnCreateServer);
		add(btnBack);
	}
	
	class StartGameActionListener implements ActionListener {
		private String button;
		
		public StartGameActionListener(String button) {
			this.button = button;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			switch (button) {
				case "Join Game":
					System.out.println("Join Server clicked");
					// add the client username that was inputted to the client_names arraylist in Constant
					Constant.addClientName(txtUsername.getText());
				try {
					new ClientWaitingScreen(ClientGameScreen.this, txtUsername.getText(), ClientGameScreen.this.txtServerName.getText());

				} catch (SocketException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
					ClientGameScreen.this.setVisible(false);
					break;
				case "Go Back":
					System.out.println("Go Back clicked");
					ClientGameScreen.this.parent.setVisible(true);
					ClientGameScreen.this.setVisible(false);
					ClientGameScreen.this.dispose();
					break;
				
			}
		}
	}

	@Override
	public void setupWidgets(DatagramSocket socket) {
		// TODO Auto-generated method stub
		
	}
}
