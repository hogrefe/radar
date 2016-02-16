package server;

import java.util.concurrent.LinkedBlockingQueue;

public class Warehouse {
	// index 0 = Socket - index 1 = Message
	private LinkedBlockingQueue<Object[]> messages = new LinkedBlockingQueue<Object[]>();
		
	public LinkedBlockingQueue<Object[]> getMessages() {
		return messages;
	}
}
