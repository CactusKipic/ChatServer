package main.ui;

import main.Message;
import main.network.SessionHandler;

public class ChatRoom {

	private static Window win;
	private static SessionHandler session;
	
	public static void initChatRoom(SessionHandler handler) {
		session = handler;
		win = new Window();
		win.setLocation(500, 200);
		win.setVisible(true);
	}
	
	public static SessionHandler getSessionHandler() {
		return session;
	}
	
	public static void newMessage(Message message) {
		win.newMessage(message);
	}
	
}
