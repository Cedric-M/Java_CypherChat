package fr.exia.cypherchat.client;

public interface ModelListener {
	
	// Quand on est connecté/déconnecté du serveur
	public void onServerConnectionChanged(boolean status);
	
	// Quand un utilisateur se connecte au chat
	public void onUserConnected(String nickname, String ip, boolean newConnection);
	
	// Quand un utilisateur se déconnecte du chat
	public void onUserDisconnected(String nickname, String ip);
	
	// Quand un utilisateur envoie un message dans le chat
	public void onMessageReceived(String nickname, String ip, String message);
	
}
