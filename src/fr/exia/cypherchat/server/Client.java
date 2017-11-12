package fr.exia.cypherchat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {

	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private Thread thread;
	private Server parent;
	private String nickname = "anonymous";

	public Client(Server parent, Socket socket) throws IOException {
		
		this.parent = parent;
		this.socket = socket; //je memorise le socket
		this.out = new PrintWriter(socket.getOutputStream(), true);
		this.in = new BufferedReader( new InputStreamReader(socket.getInputStream()));
	}
	
	public Socket getSocket() {
		return this.socket;
	}
	
	public void startPollingThread() {
		this.thread = new Thread(this);
		this.thread.start();
	}

	@Override
	public void run() {
		
		String message;
		
		while(true) 
		{
			try
			{
				message = this.in.readLine();

				if (message == null) {
					
					close(); //ferme le socket et le thread de polling
					parent.onClientDisconnected(this); //Prévenir la classe serveur que le client est deconnecté
					
					return; //On arrete le thread
				}
				parent.onClientRawDataReceived(this, message); //On previent la classe server 
				//write("ECHO -> " + message ); //TODO Temporaire
			}
			catch (IOException e) {
				System.err.println("[Server][" + socket.getInetAddress() + "] Error while receiving message");
			}
		}	
	}
	
	public boolean write(String data) {
		
		try{
			this.out.println(data);
			return true;
		}
		catch(Exception ex){
			return false;
		}
	}
	
	public boolean close() {
		
		try {
			this.thread.interrupt();  //arreter le thread
			this.in.close(); //fermer les flux
			this.out.close(); 
			this.socket.close(); //fermer le socket
			
			return true;
		}
		catch (Exception ex){
			
			return false;
		}
	}

	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
		
	}


}
