package main.ui;

import main.Message;
import main.network.SessionHandler;

public class ChatRoom {

	private static Window win;
	private static SessionHandler session;
	
	public static void initChatRoom(SessionHandler handler) {
		session = handler;
		win = new Window(session.getUsername());
		win.setLocation(500, 200);
		win.setVisible(true);
		Thread t = new Thread(session);
		t.start();
	}
	
	public static SessionHandler getSessionHandler() {
		return session;
	}
	
	public static void newMessage(Message message) {
		win.newMessage(message);
	}
	
}
