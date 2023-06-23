package squidwars.host;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultCellEditor;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Iterator;
import java.util.Scanner;

import squidwars.GameScreen;
import squidwars.entities.Wall;
import squidwars.entities.Flag;


public class HostWaitingForPlayersScreen extends GameScreen implements Runnable{
	String name;
	String mapName;
	
	String playerData;
	public static int playerCount;
	DatagramSocket serverSocket = null;
//	DatagramPacket serverPacket = 
	GameState game;
	ChatState chat;
	GameCycler cycler;
	private boolean inGame = true;
	
	int connectStage = 0;
	int numPlayers;
	
	Thread t = new Thread(this);

	DefaultTableModel tblModel;
	JTable tblPlayers;
	
	
	
	public HostWaitingForPlayersScreen(JFrame parent, String name, String mapName) {
		super(parent);
//		this.name = name;
		playerCount=0;
		this.name = "Game Server";
		this.mapName = mapName;
		System.out.println(this.name);
		setTitle("[Host] Squid Wars");
		setupServer();		
	}
	
	
	public void setupServer() {
		this.numPlayers = 4;
		try {
            serverSocket = new DatagramSocket(Constant.SERVER_PORT); // Create a DatagramSocket object and bind it to a specific port.
			serverSocket.setSoTimeout(100);
		} catch (IOException e) {
            System.err.println("Could not listen on port: "+4444);
            System.exit(-1);
		}catch(Exception e){}
		//Create the game state
		
		game = new GameState();
		game.mapName = this.mapName;
		chat = new ChatState();
		cycler = new GameCycler(game);
		
		System.out.println("Game created...");
		
		//Start the game thread
		t.start();
	}
	
	@Override
	public void setupWidgets() {
		setBackgroundImage("src/resources/windows/HostWaitingWindow.png");
		
		// Window Components
		
		/*
		JLabel title = new JLabel("Waiting for players...", SwingConstants.CENTER);
		title.setBounds(WINDOW_WIDTH/2 - TITLE_WIDTH/2, 75, TITLE_WIDTH, TITLE_HEIGHT);
 		title.setFont(new Font("sans serif", Font.PLAIN, 30));
 		*/
 		
 		// SET UP THE TABLE
 		String[] colNames = {"Players", "Team"};
 		Object[][] data = {
 		};

 		this.tblModel = new DefaultTableModel(data, colNames); // the data indicator for the table rows
 		this.tblPlayers = new JTable(tblModel);
 		
 		JComboBox<String> teamDropDown = new JComboBox<>(); // dropdown menu
 		teamDropDown.addItem("Team A");
 		teamDropDown.addItem("Team B");
 		
 		DefaultCellEditor cellEditor = new DefaultCellEditor(teamDropDown); // set the cell for the dropdown menu
 		this.tblPlayers.getColumnModel().getColumn(1).setCellEditor(cellEditor);
 		
 		// Add an ActionListener to the JComboBox in the cell editor
        teamDropDown.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox<String> teamDropDown = (JComboBox<String>) e.getSource();
                String selectedValue = (String) teamDropDown.getSelectedItem();
                int row = tblPlayers.getSelectedRow();
                int column = tblPlayers.getSelectedColumn();
                System.out.println("Selected value: " + selectedValue + " at row " + row + ", column " + column);
            
                tblModel.setValueAt(selectedValue, row, column);
                
                System.out.println(tblModel.getValueAt(row, column));
            }
        });

 		JScrollPane container = new JScrollPane(tblPlayers);
 		tblPlayers.setFillsViewportHeight(true);
 		container.setBounds(WINDOW_WIDTH/2 - 500/2, 250, 500, 100);

		JButton btnStartGame = createButton("Start Game", WINDOW_WIDTH/2 - BTN_WIDTH/2, 500, BTN_WIDTH, BTN_HEIGHT);
		btnStartGame.addActionListener(new HostWaitingForPlayerActionListener("Start Game"));
		JButton btnBack = createButton("Go Back", WINDOW_WIDTH/2 - BTN_WIDTH/2, 500+BTN_HEIGHT+15, BTN_WIDTH, BTN_HEIGHT);
		btnBack.addActionListener(new HostWaitingForPlayerActionListener("Go Back"));

//		add(title);
		add(container);
		add(btnStartGame);
		add(btnBack);
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
					if (playerCount==numPlayers){
						// Assign players' initial positions based on team assignments and selected map
						initMap();
						
						/* [To check the initial position of players]
						for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
							String name=(String)ite.next();
							PlayerState player=(PlayerState)game.getPlayers().get(name);
							System.out.println(name + "(" + String.valueOf(player.getX()) + ", " + String.valueOf(player.getY()) + ")");
						}
						*/
						
						connectStage=1;
					}
					break;
				case "Go Back":
					System.out.println("Go Back clicked");
					HostWaitingForPlayersScreen.this.parent.setVisible(true);
					HostWaitingForPlayersScreen.this.setVisible(false);
					HostWaitingForPlayersScreen.this.dispose();
					break;
				
			}
		}
	}
	
	public void broadcast(String msg){
		System.out.println("Message: " + msg);
		for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
			String name=(String)ite.next();
			PlayerState player=(PlayerState)game.getPlayers().get(name);			
			send(player,msg);	
		}
	}
	
	public void broadcast(byte[] data){
//		System.out.println("Message: " + msg);
		for(Iterator ite=game.getPlayers().keySet().iterator();ite.hasNext();){
			String name=(String)ite.next();
			PlayerState player=(PlayerState)game.getPlayers().get(name);			
			send(player,data);	
		}
	}
	
	

	public void send(PlayerState player, String msg){
		DatagramPacket packet;	
		byte buf[] = msg.getBytes();		
		packet = new DatagramPacket(buf, buf.length, player.getAddress(),player.getPort());
		try{
			serverSocket.send(packet);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}

	public void send(PlayerState player, byte[] msg){
		DatagramPacket packet;		
		packet = new DatagramPacket(msg, msg.length, player.getAddress(),player.getPort());
		try{
			serverSocket.send(packet);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	public static int getPlayerCount() {
		return playerCount;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(inGame) {
			try{
				Thread.sleep((long) 0.05);
			}catch(Exception ioe){}
			
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try{
     			serverSocket.receive(packet);
//     			String receivedMessage = new String(packet.getData(), 0, packet.getLength(), StandardCharsets.UTF_8);
//                System.out.println("Client: " + receivedMessage);
			}catch(Exception ioe){}
			
			playerData=new String(buf);
			playerData=playerData.trim();
			
			if (playerData.startsWith("CHAT")) {
//				  System.out.println("HERE!");
				  String[] chatInfo = playerData.split("<#>");
				  chat.update(chatInfo[1],chatInfo[2]);
				  broadcast("CHAT<#>"+chat.getLastMessage().toString());
			}
			
			switch(connectStage){
			  case 0:
					//System.out.println("Game State: Waiting for players...");
					if (playerData.startsWith("CONNECT") && playerCount<4){
						String tokens[] = playerData.split(" ");
						PlayerState player=new PlayerState(tokens[1],packet.getAddress(),packet.getPort());
						System.out.println("Player connected: "+tokens[1]);
						game.updatePlayers(tokens[1].trim(),player);
						broadcast("CONNECTED "+tokens[1]);
//						System.out.println("PlayerCount: " + playerCount);
						playerCount++;
//						System.out.println("PlayerCount: " + playerCount);
						DefaultTableModel model = (DefaultTableModel) tblPlayers.getModel();
						Object[] newRowData = { tokens[1], "Team" };
						
						if(playerCount <= 2) {
							newRowData[1] = "Team A";
						}
						else {
							newRowData[1] = "Team B";
						}
						model.addRow(newRowData);
						model.fireTableDataChanged();
						
					}
				  break;	
			  case 1:
				  System.out.println("Game State: START");
				  broadcast("START");
				  
				  try {
					  Thread.sleep(2);
				  } catch (InterruptedException e){}
				  
				  broadcast(Parser.toByteArray(game));
				  
				  connectStage=2;
				  break;
			  case 2:
				  //System.out.println("Game State: IN_PROGRESS");
				  
				  //Player data was received!
				  if (playerData.startsWith("PLAYER")){
					  String[] playerInfo = playerData.split(" ");					  
					  String pname =playerInfo[1];
					  int x = Integer.parseInt(playerInfo[2].trim());
					  int y = Integer.parseInt(playerInfo[3].trim());
					  char direction = playerInfo[4].charAt(0);
					  //Get the player from the game state
					  PlayerState player=(PlayerState)game.getPlayers().get(pname);					  
					  player.setX(x);
					  player.setY(y);
					  player.setDirection(direction);
					  //Update the game state
					  game.updatePlayers(pname, player);
					  //Send to all the updated game state
					  broadcast(game.toString());
				  }
				  
				  

				  if(playerData.startsWith("MOVEMENT")){
					String[] movementInfo = playerData.split(":");
					System.out.println(playerData);
//					broadcast(playerData);
					
					if(movementInfo[2].charAt(0)=='S') {
						cycler.playerShoot(movementInfo[1]);
					}
					else {
						cycler.movePlayer(movementInfo[1],movementInfo[2].charAt(0));
					}
					
				  }
				  
				  cycler.cycleGame();
				  if(game.getWin()!='X') {
					  System.out.println("MAY WINNER NA");
					  //may broadcast here to end game
					  connectStage=3;
				  }
				  
				  broadcast(Parser.toByteArray(game));
				  break;
			  case 3:
				  System.out.println("WIN: " + game.getWin());
				  broadcast("GAMEOVER:"+game.getWin());
				  this.inGame = false;
				  
				  // Go back to main menu
				  HostWaitingForPlayersScreen.this.goToMainMenu();
				  break;
			}
		}
	}

	// Get the position (row and column) of A,B,C,D in map file
	// Get the team assignment of each player
	// Set initial position of player
	//		Team A will be assigned to positions A, B
	//		Team B will be assigned to positions C, D 
	void initMap() {
    	try {
	    	File map = new File("src/maps/multiplayer/" + this.mapName);
	    	System.out.println(this.mapName);
	    	Scanner sc = new Scanner(map);
	    	for(int i=0; i<20; i++) {
	    		String row = sc.nextLine();
	    		char[] row1 = row.toCharArray();
	    		
	    		for(int j=0; j<25; j++) {
	    			if (row1[j] == 'A' || row1[j] == 'B' || row1[j] == 'C' || row1[j] == 'D') {
	    				boolean firstPlayerTeamA = true; // A if true, B if false
	    				boolean firstPlayerTeamB = true; // C if true, D if false
	    				PlayerState p = null;
	    				
	    				for (int k = 0; k < numPlayers; k++) {
	    					// Get the name and team of each player
	    					String pname = tblPlayers.getValueAt(k, 0).toString();
	    					String pteam = tblPlayers.getValueAt(k, 1).toString();
	    					
	    					// Get player assignment based on the conditions
	    					if ((row1[j] == 'A' && pteam.equalsIgnoreCase("Team A") && firstPlayerTeamA) 
	    							|| (row1[j] == 'C' && pteam.equalsIgnoreCase("Team B") && firstPlayerTeamB)) {
	    						p = game.getPlayers().get(pname);
	    						if(row1[j]=='A') {
	    							
		    						p.setTeam('A');
		    						System.out.println("Assigned A! "+row1[j]);
	    						}
	    						else {
	    							p.setTeam('B');
	    							System.out.println("Assigned B! "+row1[j]);
	    						}
	    						
	    						break;
	    					} else if (row1[j] == 'B' && pteam.equalsIgnoreCase("Team A")) {
	    						if (firstPlayerTeamA) {
	    							firstPlayerTeamA = false;
	    							continue;
	    						}
	    						p = game.getPlayers().get(pname);
	    						p.setTeam('A');
	    						System.out.println("Assigned A! "+row1[j]);
	    						break;
	    					} else if (row1[j] == 'D' && pteam.equalsIgnoreCase("Team B")) {
	    						if (firstPlayerTeamB) {
	    							firstPlayerTeamB = false;
	    							continue;
	    						}
	    						p = game.getPlayers().get(pname);
	    						p.setTeam('B');
	    						System.out.println("Assigned B! "+row1[j]);
	    						break;
	    					}
	    					
	    				}
	    				
	    				// Set initial position of the player
//	    				System.out.println("Team: "+ p.getTeam());
	    				p.setX(j*40);
	    				p.setY(i*40);
	    				p.setRespX(j*40);
	    				p.setRespY(i*40);
	    			}
	    			
	    			if (row1[j]=='W') {
	    				Wall newWall = new Wall(j*40, i*40, false);
	    				game.addWall(newWall);
	    			}
	    			if (row1[j]=='F') {
//	    				game.setFlagXY(j*40, i*40);   
	    				System.out.println("j: " + j + " i: " + i);
	    				game.setFlag(new Flag(j*40, i*40));
	    			}
		    	}
	    		
	    		
	    	}
	    	sc.close();
    	} catch (FileNotFoundException e) {
    		System.out.println("Cannot read the map file: " + this.mapName);
    	}
	}


	@Override
	public void setupWidgets(DatagramSocket socket) {
		// TODO Auto-generated method stub
		
	}
}
