package squidwars;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import squidwars.host.Constant;

public class Chatbox extends JFrame {
	private List<String> chats;
	private JPanel chatPanel;
	DatagramSocket socket;
	InetAddress serverAddress;
	String username;
	
	public static final int MAX_MESSAGES = 15;
	
	public Chatbox(DatagramSocket socket, InetAddress serverAddress, String username) {
		setTitle("Chat");
		setSize(300, 500);
		setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(false);
		addKeyListener(new TAdapter());
		
		chats = new ArrayList<>();
		this.socket = socket;
		this.serverAddress = serverAddress;
		this.username = username;
		
		setupComponents();
	}
	
	public void send(String msg){
		try{
			byte[] buf = msg.getBytes();
        	DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddress, 4444);
        	socket.send(packet);
        }catch(Exception e){}
		
	}
	
	public void addMsg(String msg) {
		chats.add(msg);
		repaintChat();
	}
	
	void setupComponents() {
		JTextField msgField = new JTextField();
		msgField.setBounds(0, 430, 300, 40);
		msgField.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				
				if (key == KeyEvent.VK_ESCAPE) {
					Chatbox.this.setVisible(false);
				} else if (key == KeyEvent.VK_ENTER) {
					String msg = msgField.getText();
					if (msg.length() != 0) {
//						chats.add(msg);
						
						if (chats.size() > Chatbox.MAX_MESSAGES) {
							chats.remove(0);
						}
						
						send("CHAT<#>"+username+":<#>"+msg);
						
						msgField.setText("");
					}
					
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		
		add(msgField);
		
		chatPanel = new JPanel();		
		chatPanel.setBounds(20, 20, 250, 400);
		chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
		chatPanel.setVisible(true);
		add(chatPanel);
	}
	
	void repaintChat() {
		chatPanel.removeAll();
		
		for (String msg: chats) {
			String format = "<html><body style='260px; margin-bottom: 20px'>%1s";
			JLabel chatContent = new JLabel(String.format(format, msg));
			chatContent.setBorder(new EmptyBorder(0, 0, 10, 0)); // bottom margin for the message
			chatPanel.add(chatContent);
		}
		
		chatPanel.validate();
	}
	
	/**
	 * Key press listeners
	 *
	 */
	private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
        	int key = e.getKeyCode();
        	
        	// Closes the chatbox window
        	if (key == KeyEvent.VK_ESCAPE) {
        		setVisible(false);
        	}
        }

        @Override
        public void keyPressed(KeyEvent e) {
        }
    }
}
