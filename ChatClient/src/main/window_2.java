package main;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class window_2 extends JFrame {
	
	static String username2;

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window_2 frame = new window_2();
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
	public window_2() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 429, 486);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		 text2 = new JTextArea();
		text2.setBounds(10, 395, 292, 52);
		contentPane.add(text2);
		
		 display2 = new JTextArea();
		display2.setBounds(10, 57, 394, 328);
		contentPane.add(display2);
		
		send2 = new JButton("ENVOYER");
		send2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s = text2.getText();
				if(s.equals("")) {
					return;
				}
				display2.append(username2 + ":" + s + "\n");
				window_1.sendText();
				text2.setText("");
			}
		});
		send2.setBounds(312, 395, 92, 52);
		contentPane.add(send2);
		
		label2 = new JLabel("Chat avec:" + username2);
		label2.setBounds(10, 10, 246, 37);
		contentPane.add(label2);
		
		JButton quitter2 = new JButton("QUITTER");
		quitter2.setBounds(305, 10, 92, 37);
		contentPane.add(quitter2);
	}

	public static void sendText() {
		String s = window_1.text1.getText();
		if(s.equals("")) {
			return;
		}
		display2.append(window_1.username1 + ":" + s + "\n");
	}
	
	private javax.swing.JLabel label2;
	private static javax.swing.JTextArea display2;
	private javax.swing.JButton send2;
	public static javax.swing.JTextArea text2;
}
