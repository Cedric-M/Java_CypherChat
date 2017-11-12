package fr.exia.cypherchat.client;

public interface ViewListener {

	public void onNicknameChanged(String newNickname);
	
	public void onMessageSent(String message);
	
	public void onCypherMethodChanged(String cypherMethod);
	
}
