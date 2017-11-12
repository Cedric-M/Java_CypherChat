package fr.exia.cypherchat;

import java.awt.EventQueue;
import java.io.IOException;

import javax.swing.UIManager;

import fr.exia.cypherchat.client.ClientWindow;
import fr.exia.cypherchat.client.Controller;
import fr.exia.cypherchat.client.Model;
import fr.exia.cypherchat.server.Server;

public class Main {

	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	public static void main(String[] args)
			throws IOException {
		
		// Lancement du serveur
		Server srv = new Server(500);
		srv.start();
		
		// Lancement du client
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				// Avoir le look windows normal
				try {
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				} catch (Exception e) { }
				
				// Créer la fenêtre
				ClientWindow view = new ClientWindow();
				
				// Créer le modèle
				Model model = new Model();
				
				// Créer le controller
				Controller ctrl = new Controller(model, view);
				
				// Afficher la fenêtre
				view.setVisible(true);
			}
		});
		
	}

}
