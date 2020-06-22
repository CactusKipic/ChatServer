package main.ui;

import main.network.SessionHandler;

public class ChatRoom {

	static Window win;
	static Server server;
	public SessionHandler session;
	
	public ChatRoom(SessionHandler session) {
		this.session = session;
		win = new Window();
		win.setLocation(500, 200);
		win.setVisible(true);
	}
	
}
