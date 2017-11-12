package fr.exia.cypherchat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server implements Runnable {

	private int port;
	private ServerSocket socket;
	private Thread acceptThread;
	
	private List<Client> connectedClients;
	private Client client;

	public Server(int port) {
		if (port < 1 || port > 65535) {
			throw new IllegalArgumentException("Invalid port");
		}
		this.port = port;
		this.connectedClients = new ArrayList<>();
	}
	
	public void start() throws IOException {
		
		this.socket = new ServerSocket(this.port); // On ouvre le socket sur le port donnée
		this.acceptThread = new Thread(this); // On fabrique un thread qui va boucler en permanence et accepter les nouvelles connexions.
		this.acceptThread.start();
		// Log
		System.out.println("[Server] Listening at port " + this.port);
	}

	@Override
	public void run() {

		while (true) 
		{
			try {
				// Cette méthode sert à attendre la connexion d'un nouveau client. Elle bloquera jusqu'à l'arrivée
				// d'une connexion. Quand un client se connectera,la méthode renverra le socket de connexion au client.
				Socket s = socket.accept();
				// Arrivé ici, cela signifie qu'une connexion a été reçue sur le port du serveur.
				System.out.println("[Server] Connection received from "
						+ s.getInetAddress());
				// Créer un objet pour réprésenter le client
				Client c = new Client(this, s);
				//on lance le thread qui se charge de lire les données qui arrivent dans le socket
				c.startPollingThread();
				//je sauvegarde mon client maintenant qu'il est bien initialisé
				synchronized (this.connectedClients) {
					this.connectedClients.add(c);
				}
				
			}
			catch (IOException e) {
				System.err.println("[Server] Client initialisation error");
				e.printStackTrace();
			}
		}
	}
	public void onClientDisconnected(Client client) {
		System.out.println("[Server][" + client.getSocket().getInetAddress() + "] Client has just been disconnected");
		
		synchronized (this.connectedClients)  //Retirer le client de la liste des clients connectés
		{ 
			this.connectedClients.remove(client);
		}
		
	}
	public void onClientRawDataReceived(Client client, String message) {
		System.out.println("[Server][" + client.getSocket().getInetAddress() + "] Received data:" + message);
		
		if (message.length() < 3) {
			System.err.println("[Server] Invalid RAW data");
			return;
		}
		String opcode = message.substring(0,4);
		
		switch(opcode) {
		
		case "MSG;" :
			//propager le message à tous les clients
			broadcastMessage(client, message.substring(4));
			break;
			
		case "NCK;" :
			//changer le nickname du client, recupere tout les caractère à partir du 5eme caractere
			client.setNickname(message.substring(4));
			System.out.println("Nickname changed:" + client.getNickname());
			break;
			
		default :
			System.err.println("[Server] Invalid OPCODE:" + opcode);
			return;
		}
		
	}
	public void broadcastMessage(Client client, String message){
		
		//Protocole
		String data = "MSG;";
		data += client.getNickname();
		data += ";";
		data += (long)(System.currentTimeMillis() /1000);
		data += ";";
		data += client.getSocket().getInetAddress();
		data += ";";
		data += message;
		
		broadcast(data);
	}
	
	public void broadcast(String message){
		
		ArrayList<Client> copy;
		synchronized (this.connectedClients){
			//on effectue une copie de la liste
			copy = new ArrayList<>(this.connectedClients);
		}

		//On parcours l'ensemble des clients
		for(Client client : copy){
			//Et on leurs envoie le message
			client.write(message);
		}
	}

}
