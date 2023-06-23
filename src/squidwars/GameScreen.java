package squidwars;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;


public abstract class GameScreen extends JFrame {
	public static final int WINDOW_WIDTH = 1000;
	public static final int WINDOW_HEIGHT = 800;

	public final int BTN_WIDTH = 200;
	public final int BTN_HEIGHT = 40;
	
	public final int TITLE_WIDTH = 400;
	public final int TITLE_HEIGHT = 100;

	public final int TEXTFIELD_WIDTH = 250;
	public final int TEXTFIELD_HEIGHT = 40;
	
	public DatagramSocket socket;
	public JFrame parent;
	
	public String clientName="default";
	public InetAddress serverAddress;
	
	public boolean status; // win/lose
	
	public GameScreen(JFrame parent) {
		this.parent = parent;
		ImageIcon img = new ImageIcon("src/resources/squid1.png");
		this.setIconImage(img.getImage());
		setupWidgets();
		configureWindow();
	}
	
	public GameScreen(JFrame parent, DatagramSocket socket) {
		ImageIcon img = new ImageIcon("src/resources/squid1.png");
		this.setIconImage(img.getImage());
		this.parent = parent;
		this.socket = socket;
		setupWidgets(socket);
		configureWindow();
	}
	
	public GameScreen(JFrame parent, DatagramSocket socket, String clientName, InetAddress serverAddress) {
		ImageIcon img = new ImageIcon("src/resources/squid1.png");
		this.setIconImage(img.getImage());
		this.parent = parent;
		this.socket = socket;
		this.clientName = clientName;
		this.serverAddress = serverAddress;
		
		setupWidgets(socket);
		configureWindow();
	}
	
	// For game over screen
	public GameScreen(JFrame parent, boolean status) {
		ImageIcon img = new ImageIcon("src/resources/squid1.png");
		this.setIconImage(img.getImage());
		this.parent = parent;
		this.status = status;
		setupWidgets();
		configureWindow();
	}
	
	protected JButton createButton(String label, int x, int y, int width, int height) {
		JButton b = new JButton(label);
		
		b.setBounds(x, y, width, height);
		
		return b;
	}

	public abstract void setupWidgets();
	public abstract void setupWidgets(DatagramSocket socket);
	
	void configureWindow() {
		// Window Setup
		setTitle("Squid Wars");
		setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}
	
	public void setBackgroundImage(String imgsrc) {
		// Set BG image of window
		setContentPane(new JLabel(new ImageIcon(imgsrc)));
	}
	
	public void goToMainMenu() {
		if (!this.getClass().getName().equals("MainMenu") && this.parent instanceof GameScreen) {
			this.parent.setVisible(true);
			GameScreen g = (GameScreen) this.parent;
			g.goToMainMenu();
			this.dispose();
		}
	}
}
