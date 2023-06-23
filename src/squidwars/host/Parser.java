package squidwars.host;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import squidwars.entities.*;

public class Parser {
	/**
	 * Converts the GameState object to a byte array
	 * 
	 * Refs:
	 * https://stackoverflow.com/questions/10358981/how-to-send-object-over-datagram-socket
	 * https://stackoverflow.com/questions/5683486/how-to-combine-two-byte-arrays
	 */
	public static byte[] toByteArray(Object o) {
		try {
			final ByteArrayOutputStream baos = new ByteArrayOutputStream(12800);
			final ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject((GameState)o);
			byte[] header = "GAME STATE#".getBytes();
			byte[] body = baos.toByteArray();
			
			// Concatenate header and body byte arrays
			final byte[] data = new byte[header.length + body.length];
			System.arraycopy(header, 0, data, 0, header.length);
			System.arraycopy(body, 0, data, header.length, body.length);

			
			return data;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Converts a byte array back to a GameState object
	 */
	public static GameState parseBuffer(byte[] buffer) {
		try {			
			byte[] body = Arrays.copyOfRange(buffer, 11, buffer.length);
			ByteArrayInputStream in = new ByteArrayInputStream(body);
			ObjectInputStream is = new ObjectInputStream(in);
			return (GameState)is.readObject();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("[parseBuffer] Invalid buffer input!");
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("[parseBuffer] GameState class not found.");
			return null;
		}
	}
}
