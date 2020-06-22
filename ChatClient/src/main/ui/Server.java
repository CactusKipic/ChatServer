package main.ui;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import main.network.SessionHandler;

public class Server extends JFrame implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6358558985516499831L;
	private JPanel contentPane = new JPanel();
	private JTextField username= new JTextField();
	private JTextField sessionname= new JTextField();
	private JTextField ip = new JTextField();
	private JTextField port = new JTextField();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.out.println("Lancement server");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server frame = new Server();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Server() {

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(500,300));
		contentPane.setSize(500,250);
		setContentPane(contentPane);
		
		Dimension txtDim = new Dimension(200,60);

		JTextArea jtxtuser = new JTextArea();
		jtxtuser.setText("Username:");
		jtxtuser.setSize(100,60);
		contentPane.add(jtxtuser);
		
		username = new JTextField();
		username.setMinimumSize(txtDim);
		username.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				createRoom();
			}
		});
		contentPane.add(username);
		username.setColumns(12);
		
		
		JTextArea jtxtsession = new JTextArea();
		jtxtsession.setText("Session name:");
		jtxtsession.setSize(100,60);
		contentPane.add(jtxtsession);
		
		sessionname = new JTextField();
		sessionname.setMinimumSize(txtDim);
		sessionname.addActionListener(this);
		contentPane.add(sessionname);
		sessionname.setColumns(12);
		
		JTextArea jtxtip = new JTextArea();
		jtxtip.setText("IP Server:");
		jtxtip.setSize(100,60);
		contentPane.add(jtxtip);
		
		ip.addActionListener(this);
		ip.setMinimumSize(txtDim);
		contentPane.add(ip);
		ip.setColumns(8);

		JTextArea jtxtport = new JTextArea();
		jtxtport.setText(":");
		jtxtport.setSize(25,60);
		contentPane.add(jtxtport);
		
		port.addActionListener(this);
		port.setMinimumSize(txtDim);
		contentPane.add(port);
		port.setColumns(4);
		
		JButton btnNewButton = new JButton("REJOINDRE SESSION");
		btnNewButton.addActionListener(this);
		contentPane.add(btnNewButton);
		pack();
	}
	
	private void createRoom() {
		String p1, p2;
		
		p1 = username.getText();
		p2 = ip.getText();
		
		if(p1.equals("") || p2.equals("")) {
			JOptionPane.showMessageDialog(null, "Veuillez entrer un username valide.");
			return;
		}
		
	
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		switch(e.getActionCommand()) {
			case "REJOINDRE SESSION":
				System.out.println("Rejoindre session pressed");
				if(ip.getText() != "" && port.getText() != "" && username.getText() != "" && sessionname.getText() != "")
				ChatRoom.initChatRoom(new SessionHandler(ip.getText(), Integer.parseInt(port.getText()),
						username.getText(), sessionname.getText()));
				else 
					JOptionPane.showMessageDialog(null, "Remplissez chacun des champs.");
				break;
		}
		
	}
}
