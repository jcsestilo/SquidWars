package squidwars;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.DatagramSocket;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;


public class MainMenu extends GameScreen {	
	public MainMenu() {
		super(null);
	}
	
	@Override
	public void setupWidgets() {
		setBackgroundImage("src/resources/windows/MainMenuWindow.png");
		
		

		
		// Window Components
		/*
		JLabel title = new JLabel("Squid Wars", SwingConstants.CENTER);
		title.setBounds(WINDOW_WIDTH/2 - TITLE_WIDTH/2, 200, TITLE_WIDTH, TITLE_HEIGHT);
		title.setFont(new Font("sans serif", Font.PLAIN, 50));
		*/

		JButton btnStart = createButton("Start", WINDOW_WIDTH/2 - BTN_WIDTH/2, 350, BTN_WIDTH, BTN_HEIGHT);
		btnStart.addActionListener(new MainMenuActionListener("Start"));
		JButton btnOptions = createButton("Options", WINDOW_WIDTH/2 - BTN_WIDTH/2, 350+BTN_HEIGHT+15, BTN_WIDTH, BTN_HEIGHT);
		btnOptions.addActionListener(new MainMenuActionListener("Options"));
		JButton btnExit = createButton("Exit", WINDOW_WIDTH/2 - BTN_WIDTH/2, 350+2*BTN_HEIGHT+30, BTN_WIDTH, BTN_HEIGHT);
		btnExit.addActionListener(new MainMenuActionListener("Exit"));

//		add(title);
		add(btnStart);
		add(btnOptions);
		add(btnExit);
	}
	
	class MainMenuActionListener implements ActionListener {
		private String button;
		
		public MainMenuActionListener(String button) {
			this.button = button;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			switch (button) {
				case "Start":
					System.out.println("Start clicked");
					new StartGame(MainMenu.this);
					MainMenu.this.setVisible(false);
					break;
				case "Options":
					System.out.println("Options clicked");
					break;
				case "Exit":
					System.out.println("Exit clicked");
					int result = JOptionPane.showConfirmDialog(MainMenu.this, "Are you sure?", "Exit Squid Wars", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (result == JOptionPane.YES_OPTION) {
						MainMenu.this.dispatchEvent(new WindowEvent(MainMenu.this, WindowEvent.WINDOW_CLOSING));
					}
					break;
				
			}
		}
	}

	@Override
	public void setupWidgets(DatagramSocket socket) {
		// TODO Auto-generated method stub
		
	}
}
