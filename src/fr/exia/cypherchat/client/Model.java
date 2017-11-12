package fr.exia.cypherchat.client;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Model {

	private String nickname = null;
	
	private String cypherMethod = null;
	
	private String serverAddress = null;
	
	private List<String> connectedUsers;
	
	private List<ModelListener> listeners;
	
	public Model() {
		this.connectedUsers = new ArrayList<>();
		this.listeners = new ArrayList<>();
	}
	
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getCypherMethod() {
		return cypherMethod;
	}

	public void setCypherMethod(String cypherMethod) {
		this.cypherMethod = cypherMethod;
	}

	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	public List<String> getConnectedUsers() {
		return connectedUsers;
	}

	public void addConnectedUser(String username) {
		this.connectedUsers.add(username);
	}
	
	public void removeConnectedUser(String username) {
		this.connectedUsers.remove(username);
	}

	public void addListener(ModelListener listener) {
		this.listeners.add(listener);
	}
	
	public void removeListener(ModelListener listener) {
		this.listeners.remove(listener);
	}
	
	public void notifyEvent(String methodName, Object... args) {
		// Les trois petits points sont une élipse
		// Object...   =   Object[n]
		
		// Chercher la bonne méthode dans l'interface
		Method methodCall = null;
		for (Method method : ModelListener.class.getMethods()) {
			if (methodName.equals(method.getName())) {
				methodCall = method;
				break;
			}
		}
		if (methodCall == null) {
			throw new IllegalArgumentException("Event " + methodName
					+ " doesn't exist");
		}
		
		// Parcourir les listeners
		for (ModelListener listener : this.listeners) {
			// Appeler la méthode sur le listener
			try {
				methodCall.invoke(listener, args);
			}
			catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				System.err.println("Erreur lors du dispatch de l'event " 
						+ methodName + " sur le listener " 
						+ listener.getClass());
				e.printStackTrace();
			}
		}
	}
	
}
