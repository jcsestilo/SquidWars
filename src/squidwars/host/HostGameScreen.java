package squidwars.host;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.DatagramSocket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import squidwars.GameScreen;


public class HostGameScreen extends GameScreen {
	String servername;
	
	JComboBox<String> cbMap;

	public HostGameScreen(JFrame parent) {
		super(parent);
		setTitle("Squid Wars - Host a game");
	}
	
	@Override
	public void setupWidgets() {
		String[] mapOptions = loadMaps();
		
		setBackgroundImage("src/resources/windows/HostWindow.png");
		
		// Window Components
		/*
		JLabel title = new JLabel("HOST", SwingConstants.CENTER);
		title.setBounds(WINDOW_WIDTH/2 - TITLE_WIDTH/2, 75, TITLE_WIDTH, TITLE_HEIGHT);
 		title.setFont(new Font("sans serif", Font.PLAIN, 30));
		*/
		
		// Map
		JLabel lblMap = new JLabel("Map");
		lblMap.setBounds(WINDOW_WIDTH/2 - TEXTFIELD_WIDTH/2, 325, 100, 30);
 		lblMap.setFont(new Font("sans serif", Font.BOLD, 12));
 		lblMap.setForeground(Color.WHITE);
 		cbMap = new JComboBox<String>(mapOptions);
		cbMap.setBounds(WINDOW_WIDTH/2 - TEXTFIELD_WIDTH/2, 325+30, TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT);
		
		// Friendly Fire
		/*
		JCheckBox ckbFriendlyFire = new JCheckBox("Friendly Fire", true);
		ckbFriendlyFire.setBounds(WINDOW_WIDTH/2 - TEXTFIELD_WIDTH/2, 365+50, TEXTFIELD_WIDTH, TEXTFIELD_HEIGHT);
		*/
		
		JButton btnCreateServer = createButton("Create Server", WINDOW_WIDTH/2 - BTN_WIDTH/2, 500, BTN_WIDTH, BTN_HEIGHT);
		btnCreateServer.addActionListener(new StartGameActionListener("Create Server", ""));
		JButton btnBack = createButton("Go Back", WINDOW_WIDTH/2 - BTN_WIDTH/2, 500+BTN_HEIGHT+15, BTN_WIDTH, BTN_HEIGHT);
		btnBack.addActionListener(new StartGameActionListener("Go Back", ""));

//		add(title);
		add(lblMap);
		add(cbMap);
//		add(ckbFriendlyFire);
		add(btnCreateServer);
		add(btnBack);
	}
	
	class StartGameActionListener implements ActionListener {
		private String button;
		private String text;
		
		public StartGameActionListener(String button, String text) {
			this.button = button;
			this.text = text;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			switch (button) {
				case "Create Server":
					System.out.println("Create Server clicked");
					new HostWaitingForPlayersScreen(HostGameScreen.this, this.text, (String)HostGameScreen.this.cbMap.getSelectedItem());
					HostGameScreen.this.setVisible(false);
					break;
				case "Go Back":
					System.out.println("Go Back clicked");
					HostGameScreen.this.parent.setVisible(true);
					HostGameScreen.this.setVisible(false);
					HostGameScreen.this.dispose();
					break;
				
			}
		}
		
		
	}

	@Override
	public void setupWidgets(DatagramSocket socket) {
		// TODO Auto-generated method stub
		
	}
	
	// Returns the file names of the maps inside "src/maps/multiplayer" directory
	String[] loadMaps() {
		File mapDir = new File("src/maps/multiplayer");
		
		File maps[] = mapDir.listFiles();
		String mapNames[] = new String[maps.length];
		for (int i = 0; i < maps.length; i++) {
			mapNames[i] = maps[i].getName();
		}
		
		return mapNames;
	}
}
