package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ServerThread implements Runnable {
	
	Socket socket;
	
	public ServerThread(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try {
			
			System.out.println("Otvorena konekcija sa klijentom sa adresom " + socket.getInetAddress().getHostAddress() + " na portu " + socket.getLocalPort()); 
			
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
			
			String action = in.readLine();
			
			while (!action.equals("EXIT")) {
				
				String fileName = in.readLine();
				
				File f;
				
				int lineCnt;
				
				FileReader fr;
				BufferedReader br;
				PrintWriter pw;
				
				String content;
				String line;
				
				switch(action) {
				// stize fajl i vraca se sa ispravljenim greskama
				case "EDTF":
					
					lineCnt = Integer.parseInt(in.readLine());
					
					if (lineCnt == -1) {
						break;
					}
					
					f = new File("./ServerFiles/" + fileName);
					
					if (!f.exists()) {
						f.createNewFile();
					}
					
					pw = new PrintWriter(f);
					
					content = "";
					
					for (int i = 0; i < lineCnt; i++) {
						line = in.readLine();
						
						String[] words = line.split(" ");
						
						line = "";
						
						for (int j = 0; j < words.length; j++)
							if (words[j].equals(words[j].toUpperCase()) || words[j].equals(words[j].toUpperCase())) {
								line = line + words[j] + " ";
							}
							else {
								line = line + words[j].charAt(0) + (words[j].substring(1)).toLowerCase() + " ";
							}
						
						pw.println(line);
						content = content + (i == 0 ? "" : "\n") + line;
					}
					
					pw.close();
					
					out.println(content);
					
					break;
				// vraca se trazeni fajl
				case "GETF":
					
					f = new File("./ServerFiles/" + fileName);
					
					if (!f.exists()) {
						out.println("-1");
						break;
					}
					
					fr = new FileReader("./ServerFiles/" + fileName);
					br = new BufferedReader(fr);
					
					lineCnt = 0;
					
					content = "";
					
					while ((line = br.readLine()) != null) {
						lineCnt++;
						content = content + (lineCnt == 1 ? "" : "\n") + line;
					}

					out.println(lineCnt);
					out.println(content);
					
					break;
				// stize fajl
				case "SNDF":
					
					lineCnt = Integer.parseInt(in.readLine());
					
					if (lineCnt == -1) {
						break;
					}
					
					f = new File("./ServerFiles/" + fileName);
					
					if (!f.exists()) {
						f.createNewFile();
					}
					
					pw = new PrintWriter(f);
					
					for (int i = 0; i < lineCnt; i++) {
						pw.println(line = in.readLine());
					}
					
					pw.close();
					
					break;
				}
				
				// sta nam je klijent poslao
				action = in.readLine();
			}
			
			socket.close();
			System.out.println("Zatvorena konekcija sa klijentom sa adresom " + socket.getInetAddress().getHostAddress() + " na portu " + socket.getLocalPort()); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
