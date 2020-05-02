package app;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {

	public ServerMain() throws Exception {
		
		int port = 1999;
		
		ServerSocket serverSocket = new ServerSocket(port);
		System.out.println("Otvoren je port broj " + port);
		
		while (true) {
			
			Socket socket = serverSocket.accept();
			ServerThread server_thread = new ServerThread(socket);
			Thread thread = new Thread(server_thread);
			
			thread.start();
		}
		
	}
	
	public static void main(String[] args) {
		try {
			new ServerMain();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
