package main.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import main.Message;

public class Window extends JFrame implements ActionListener{
	
	private String username;
	private javax.swing.JLabel label1;
	private javax.swing.JTextArea display;
	private javax.swing.JButton send1;
	private javax.swing.JTextArea text1;

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	
	public Window(String username) {
		
		this.username = username;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 429, 486);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		text1 = new JTextArea();
		text1.setBounds(10, 395, 292, 52);
		contentPane.add(text1);
		
		display = new JTextArea();
		display.setBounds(10, 57, 394, 328);
		contentPane.add(display);
		
		send1 = new JButton("ENVOYER");
		send1.addActionListener(this);
		send1.setBounds(312, 395, 92, 52);
		contentPane.add(send1);
		
		label1 = new JLabel("Chat en tant que:" + this.username);
		label1.setBounds(10, 10, 246, 37);
		contentPane.add(label1);
		
		JButton quitter1 = new JButton("QUITTER");
		quitter1.setBounds(305, 10, 92, 37);
		contentPane.add(quitter1);
	}
	
	public String getUsername() {
		return username;
	}

	public void sendText() {/*
		String s = window_2.text2.getText();
		if(s.equals("")) {
			return;
		}
		display.append(window_2.username2 + ":" + s + "\n");*/
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Action performed");
		switch(e.getActionCommand()) {
			case "ENVOYER":
				System.out.println("ENVOYER");
				ChatRoom.getSessionHandler().sendMessage(new Message(username, text1.getText(),
						new SimpleDateFormat("yyyy-MM-dd").format(new Date())));
				text1.setText("");
				break;
		}
	}

	public void newMessage(Message message) {
		display.append(message.getDate()+" | "+message.getAuthor()+": "+message.getMessage()+"\n");
	}
	
}
