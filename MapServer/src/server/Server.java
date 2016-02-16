package server;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Server {
	int port = 8989;
	private ServerSocket serverSocket;

	private Set<Socket> clients = new HashSet<Socket>();

	public static void main(String[] args) throws Exception {
		Server server = new Server();
		server.run();
	}

	public void run() throws Exception {
		serverSocket = new ServerSocket(port);
		serverSocket.setReuseAddress(true);

		Warehouse warehouse = new Warehouse();

		Thread serverThread = null;
		Thread clientThread = null;

		serverThread = new Thread(new ServerThread(serverSocket, clients, warehouse));
		serverThread.start();

		clientThread = new Thread(new ClientThread(clients, warehouse));
		clientThread.start();
	}
}
