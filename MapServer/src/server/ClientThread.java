package server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ClientThread implements Runnable {
	private Set<Socket> clients = new HashSet<Socket>();
	private Warehouse warehouse;

	public ClientThread(Set<Socket> clients, Warehouse warehouse) {
		super();
		this.clients = clients;
		this.warehouse = warehouse;
	}

	public void run() {
		while (true) {
			synchronized (warehouse) {
				while (warehouse.getMessages().size() != 0) {
					Object[] oneMessage = warehouse.getMessages().poll();

					for (Socket socket : this.clients) {
						if (!socket.getRemoteSocketAddress()
								.equals(((Socket) oneMessage[0]).getRemoteSocketAddress())) {
							new Thread() {
								public void run() {
									synchronized (this) {
										PrintStream PS = null;
										try {
											PS = new PrintStream(socket.getOutputStream());
										} catch (IOException e) {
											e.printStackTrace();
										}
										PS.println(oneMessage[1]);
									}
								};
							}.start();
						}
					}

				}
				try {
					warehouse.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
//			try {
//				Thread.sleep((int) 500);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
	}

}
