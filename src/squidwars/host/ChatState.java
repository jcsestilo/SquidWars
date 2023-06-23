package squidwars.host;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;

public class ChatState {
//	private Map<String, String> chats = new HashMap();
	private List<Chat> chats = new ArrayList<Chat>();
	public ChatState() {}
	
	// The client will send chat to the server
	public void update(String sender, String message) {
		chats.add(new Chat(sender, message));
	}
	
	public void receiveChat(String sender, String message) {
		
	}
	
	public Chat getLastMessage() {
		return this.chats.get(chats.size()-1);
	}
}
