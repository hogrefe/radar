package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ServerThread implements Runnable {
	private ServerSocket serverSocket;
	private Set<Socket> clients = new HashSet<Socket>();
	private Warehouse warehouse;

	public ServerThread(ServerSocket serverSocket, Set<Socket> clients, Warehouse warehouse) {
		this.serverSocket = serverSocket;
		this.clients = clients;
		this.warehouse = warehouse;
	}
	
	public void run() {
		while (true) {
			Socket socket = null;
			try {
				socket = serverSocket.accept();
			} catch (IOException e) {
				e.printStackTrace();
			}
			synchronized (warehouse) {
				InputStreamReader streamReader = null;
				BufferedReader bufferedReader = null;
				clients.add(socket);
				
				if (!socket.isInputShutdown()) {
					try {
						streamReader = new InputStreamReader(socket.getInputStream());
					} catch (IOException e) {
						e.printStackTrace();
					}
					bufferedReader = new BufferedReader(streamReader);
	
					String message = null;
					try {
						message = socket.getRemoteSocketAddress() + "#" + bufferedReader.readLine();
					} catch (IOException e) {
						e.printStackTrace();
					}
	
					warehouse.getMessages().offer(new Object[] { socket, message });
				}

				warehouse.notifyAll();

			}
//			try {
//				Thread.sleep((int) 500);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
		}
	}

}