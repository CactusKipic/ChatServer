package main.core;

import main.network.ClientHandler;

public class Client {
	
	private final String name;
	private final ClientHandler handler;
	
	public Client(String name, ClientHandler handler) {
		this.name = name;
		this.handler = handler;
	}
	
	public String getName() {
		return name;
	}
	
	public ClientHandler getClientHandler() {
		return handler;
	}
	
}
