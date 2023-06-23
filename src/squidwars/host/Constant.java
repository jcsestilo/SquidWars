package squidwars.host;

import java.util.ArrayList;

public class Constant {
	public static final int SERVER_PORT = 4444;
	public static final int BUFFER_SIZE = 1024;
	public static final int spriteHeight = 40;
	public static final int spriteWidth = 40;
	
	public static ArrayList<String> client_names = new ArrayList<>();
	public static String[] squidFiles = {
			"src/resources/squid1.png",
			"src/resources/squid2.png",
			"src/resources/squid3.png",
			"src/resources/squid4.png",
	};
	
	public static void addClientName(String name) {
		client_names.add(name);
	}
}
