package squidwars.client;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import squidwars.GameScreen;
import squidwars.StartGame;
import squidwars.host.Constant;
import squidwars.host.GameState;
import squidwars.host.HostWaitingForPlayersScreen;
import squidwars.singleplayer.SinglePlayerStage;
import squidwars.Chatbox;


public class ClientWaitingScreen extends GameScreen implements Runnable {
	// The last item from the client_names will be the one put here
	String name=Constant.client_names.get(Constant.client_names.size() - 1); 
	String pname;
	InetAddress serverAddress;
	boolean connected=false;
	DatagramSocket socket = null;
	String serverData;
	Thread t=new Thread(this);
	Chatbox chatbox;

	
	int temp=0;
	
	public ClientWaitingScreen(JFrame parent, String name, String serverName) throws SocketException{
		super(parent);
		this.pname= name;
		System.out.println("NAme: "+this.pname);
		setTitle("[Client] Squid Wars");
		try {
			serverAddress = InetAddress.getByName(serverName);
			socket = new DatagramSocket();
			socket.setSoTimeout(100);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		t.start();
		chatbox = new Chatbox(this.socket, this.serverAddress, this.pname);
//		setupConnection();
	}
	
	
	
	@Override
	public void setupWidgets() {

		setBackgroundImage("src/resources/windows/ClientWaitingWindow.png");
		
		// Window Components
		/*
		JLabel title = new JLabel("Waiting for host to start the game...", SwingConstants.CENTER);
		title.setBounds(WINDOW_WIDTH/2 - (TITLE_WIDTH+300)/2, 75, TITLE_WIDTH+300, TITLE_HEIGHT);
 		title.setFont(new Font("sans serif", Font.PLAIN, 30));
 		*/
 		
 		String[] colNames = {"Players", "Team"};
 		Object[][] data = {

 		};
 		
 		JTable tblPlayers = new JTable(data, colNames);
 		JScrollPane container = new JScrollPane(tblPlayers);
 		tblPlayers.setFillsViewportHeight(true);
 		container.setBounds(WINDOW_WIDTH/2 - 500/2, 250, 500, 100);

		JButton btnBack = createButton("Quit", WINDOW_WIDTH/2 - BTN_WIDTH/2, 500+BTN_HEIGHT+15, BTN_WIDTH, BTN_HEIGHT);
		btnBack.addActionListener(new HostWaitingForPlayerActionListener("Go Back"));
//		btnBack.addActionListener(new ActionListener() {
//		    
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				// TODO Auto-generated method stub
//		    	send("CHAT<#>Test<#>Test Message");
////		    	chatbox.show();
//		    	chatbox.setVisible(true);
//			}
//		});
		
		JButton btnChat = createButton("Chat", WINDOW_WIDTH/2 - BTN_WIDTH/2, 500+2*BTN_HEIGHT+30, BTN_WIDTH, BTN_HEIGHT);
//		btnBack.addActionListener(new HostWaitingForPlayerActionListener("Go Back"));
		btnChat.addActionListener(new ActionListener() {
		    
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
//		    	send("CHAT<#>Test<#>Test Message");
//		    	chatbox.show();
		    	chatbox.setVisible(true);
			}
		});

//		add(title);
		add(container);
		add(btnBack);
		add(btnChat);
	}
	
	class HostWaitingForPlayerActionListener implements ActionListener {
		private String button;
		
		public HostWaitingForPlayerActionListener(String button) {
			this.button = button;
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			switch (button) {
				case "Start Game":
					System.out.println("Start Game clicked");
					break;
				case "Go Back":
					System.out.println("Go Back clicked");
					ClientWaitingScreen.this.parent.setVisible(true);
					ClientWaitingScreen.this.setVisible(false);
					ClientWaitingScreen.this.dispose();
					break;
			}
		}
	}
	

	public void send(String msg){
		try{
			byte[] buf = msg.getBytes();
        	DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddress, Constant.SERVER_PORT);
        	socket.send(packet);
        }catch(Exception e){}
		
	}
	
	

	@Override
	public void run() {
		while(true) {
			try{
				Thread.sleep(1);
			}catch(Exception ioe){}
			
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddress, Constant.SERVER_PORT);
			try{
	 			this.socket.receive(packet);
			}catch(Exception ioe) {}
			
			serverData=new String(buf);
			serverData=serverData.trim();
			
			if (!connected && serverData.startsWith("CONNECTED")){
				connected=true;
				System.out.println("Connected.");

			}else if (!connected){
				System.out.println("Connecting..");				
				send("CONNECT "+name);
				
				
			}else if (connected){
				
				if (serverData.startsWith("PLAYER")){
					String[] playersInfo = serverData.split(":");
				}
				if (serverData.startsWith("CHAT")) {
					String[] chatInfo = serverData.split("<#>");
					System.out.println(chatInfo[1]+" "+chatInfo[2]);
					this.chatbox.addMsg(chatInfo[1]+" "+chatInfo[2]);
				}
				
				if (serverData.startsWith("START")) {
					ClientWaitingScreen.this.setVisible(false);
					
					//chore add initial game data to start
					GameState gamedata = new GameState();
					
//					EventQueue.invokeLater(() -> {
//			            JFrame ex = new SinglePlayerStage(ClientWaitingScreen.this, socket);
//			            ex.setVisible(true);
//			        });
					
					EventQueue.invokeLater(() -> {
			            JFrame ex = new MultiPlayerStage(ClientWaitingScreen.this, socket, gamedata, this.pname, this.serverAddress);
			            ex.setVisible(true);
			        });
					
					break; // Waiting for packets will now be done in SinglePlayerStage -> ThreadAnim 
				}
			}
		}
	}



	@Override
	public void setupWidgets(DatagramSocket socket) {
		// TODO Auto-generated method stub
		
	}
}
