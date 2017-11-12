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
		//je memorise le socket
		this.socket = socket;
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
		//tant que l'application tourne
		while(true) {
			// Lire this.in pour avoir la prochaine ligne
		try {
			message = this.in.readLine();
			//Le client vient de se deconnecter
			if (message == null) {
				//ferme le socket et le thread de polling
				close();
				//Prévenir al classe serveur que le client est deconnecté
				parent.onClientDisconnected(this);
				//On arrete le thread
				return;
			}
			//log
			//System.out.println("[Server][" + socket.getInetAddress() + "] Received message: " + message);
			//On previent la classe server 
			parent.onClientRawDataReceived(this, message);
			//TODO Temporaire
			//write("ECHO -> " + message );
		} catch (IOException e) {
			System.err.println("[Server][" + socket.getInetAddress() + "] Error while receiving message");
		}
		}
		
	}
	
	public boolean write(String data) {
		try {
			this.out.println(data);
			return true;
		}
		catch(Exception ex){
			return false;
		}
	}
	
	public boolean close() {
		try {
			//arreter le thread
			this.thread.interrupt();
			//fermer les flux
			this.in.close();
			this.out.close();
			//fermer le socket
			this.socket.close();
			return true;
		}
		catch (Exception ex){
			return false;
		}
	}

	public String getNickname() {
		return this.nickname;
	}

	public void setNickname(String substring, String nickname) {
		this.nickname = nickname;
		
	}


}
