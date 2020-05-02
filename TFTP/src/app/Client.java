package app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	
	public Client() throws Exception {
		
		int port = 1999;
		
		Socket socket = new Socket("localhost", port);
		
		System.out.println("Socekt je otvoren.");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
		
		Scanner keyboard = new Scanner(System.in);
		
		// poruka sa tastature
		System.out.println("Pocni da unosis instrukcije.");
		String tmsg = keyboard.nextLine();
		
		// kraj je kada klijent unese "EXIT"
		while(!tmsg.equals("EXIT")) {
			
			String action = tmsg.substring(0, 4);
			out.println(action);
			
			String fileName = tmsg.substring(5);
			out.println(fileName);
			
			File f;
			
			int lineCnt;
			
			FileReader fr;
			BufferedReader br;
			PrintWriter pw;
			
			String content;
			String line;
			
			switch(action) {
			// salje se fajl i vraca se sa ispravljenim greskama
			case "EDTF":
				
				f = new File("./ClientFiles/" + fileName);
				
				if (!f.exists()) {
					out.println("-1");
					System.out.println("Trazeni fajl ne postoji.");
					break;
				}
				
				fr = new FileReader("./ClientFiles/" + fileName);
				br = new BufferedReader(fr);
				
				lineCnt = 0;
				
				content = "";
				
				while ((line = br.readLine()) != null) {
					lineCnt++;
					// da nemamo praznu liniju na kraju fajla
					content = content + (lineCnt == 1 ? "" : "\n") + line;
				}

				out.println(lineCnt);
				out.println(content);
				
				br.close();
				
				pw = new PrintWriter(f);
				
				for (int i = 0; i < lineCnt; i++)
					pw.println(in.readLine());
				
				pw.close();
				
				break;
			// vraca se trazeni fajl
			case "GETF":
				
				lineCnt = Integer.parseInt(in.readLine());
				
				if (lineCnt == -1) {
					System.out.println("Trazeni fajl ne postoji.");
					break;
				}
				
				f = new File("./ClientFiles/" + fileName);
				
				if (!f.exists()) {
					f.createNewFile();
				}
				
				pw = new PrintWriter(f);
				
				for (int i = 0; i < lineCnt; i++)
					pw.println(in.readLine());
				
				pw.close();
				
				break;
			// salje se dati fajl
			case "SNDF":
				
				f = new File("./ClientFiles/" + fileName);
				
				if (!f.exists()) {
					out.println("-1");
					System.out.println("Trazeni fajl ne postoji.");
					break;
				}
				
				fr = new FileReader("./ClientFiles/" + fileName);
				br = new BufferedReader(fr);
				
				content = "";
				
				lineCnt = 0;
				
				while ((line = br.readLine()) != null) {
					lineCnt++;
					// da nemamo praznu liniju na kraju fajla
					content = content + (lineCnt == 1 ? "" : "\n") + line;
				}

				out.println(lineCnt);
				out.println(content);
				
				break;
			}
			
			tmsg = keyboard.nextLine();
			
		}
		
		out.println(tmsg);
		
		keyboard.close();
		socket.close();
		System.out.println("Socket je zatvoren.");
	}

	public static void main(String[] args) {
		try {
			new Client();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
