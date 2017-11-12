package fr.exia.cypherchat.client;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class ClientWindow extends JFrame {

	private static final long serialVersionUID = 2371488770405089420L;

	private JPanel contentPane;
	
	protected JPanel panelLeft;
	protected JPanel panelCenter;
	protected JPanel panelBottom;
	protected JTextField messageField;
	protected JTextField nicknameField;
	private JScrollPane scrollPane2;
	private JList connectedUsersList;
	private JComboBox cypherComboBox;
	private JTextArea messageArea;
	
	private List<ViewListener> listeners;

	/**
	 * Create the frame.
	 */
	public ClientWindow() {
		this.listeners = new ArrayList<>();
		setTitle("CypherChat v1.0");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 738, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		panelLeft = new JPanel();
		panelBottom = new JPanel();
		panelCenter = new JPanel();
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(panelBottom, GroupLayout.DEFAULT_SIZE, 712, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(panelLeft, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelCenter, GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(panelCenter, GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE)
						.addComponent(panelLeft, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 384, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panelBottom, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE))
		);
		
		nicknameField = new JTextField();
		nicknameField.setHorizontalAlignment(SwingConstants.CENTER);
		nicknameField.setFont(new Font("Tahoma", Font.PLAIN, 18));
		nicknameField.setText("Bob");
		nicknameField.setColumns(10);
		nicknameField.setBorder(null);
		nicknameField.setBackground(null);
		
		scrollPane2 = new JScrollPane();
		
		cypherComboBox = new JComboBox();
		cypherComboBox.setModel(new DefaultComboBoxModel(new String[] {"Base64"}));
		
		GroupLayout gl_panelLeft = new GroupLayout(panelLeft);
		gl_panelLeft.setHorizontalGroup(
			gl_panelLeft.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panelLeft.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelLeft.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
						.addComponent(nicknameField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
						.addComponent(cypherComboBox, Alignment.LEADING, 0, 144, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panelLeft.setVerticalGroup(
			gl_panelLeft.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelLeft.createSequentialGroup()
					.addContainerGap()
					.addComponent(nicknameField, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane2, GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(cypherComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		
		connectedUsersList = new JList();
		scrollPane2.setViewportView(connectedUsersList);
		panelLeft.setLayout(gl_panelLeft);
		
		messageField = new JTextField();
		messageField.setColumns(10);
		
		GroupLayout gl_panelBottom = new GroupLayout(panelBottom);
		gl_panelBottom.setHorizontalGroup(
			gl_panelBottom.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelBottom.createSequentialGroup()
					.addContainerGap()
					.addComponent(messageField, GroupLayout.DEFAULT_SIZE, 692, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_panelBottom.setVerticalGroup(
			gl_panelBottom.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelBottom.createSequentialGroup()
					.addContainerGap()
					.addComponent(messageField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panelBottom.setLayout(gl_panelBottom);
		panelCenter.setLayout(new CardLayout(0, 0));
		
		JScrollPane scrollPane1 = new JScrollPane();
		panelCenter.add(scrollPane1, "name_852663272761764");
		
		messageArea = new JTextArea();
		messageArea.setEditable(false);
		scrollPane1.setViewportView(messageArea);
		contentPane.setLayout(gl_contentPane);
		
		messageField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// R�cup�rer le texte saisi dans le champ de texte
				String message = messageField.getText();
				// Notifier l'event
				notifyEvent("onMessageSent", message);
				// Vider le champ de formulaire
				messageField.setText("");
			}
		});
		
		nicknameField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				notifyEvent("onNicknameChanged", nicknameField.getText());
			}
		});
	}
	
	public void addListener(ViewListener listener) {
		this.listeners.add(listener);
	}
	
	public void removeListener(ViewListener listener) {
		this.listeners.remove(listener);
	}
	
	public void notifyEvent(String methodName, Object... args) {
		// Les trois petits points sont une élipse
		// Object...   =   Object[n]
		
		// Chercher la bonne méthode dans l'interface
		Method methodCall = null;
		for (Method method : ViewListener.class.getMethods()) {
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
		for (ViewListener listener : this.listeners) {
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
