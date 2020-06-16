package main;

import java.util.HashMap;

import main.core.Client;

public class SessionServer extends Session{
	
	private HashMap<String, Client> ListClients = new HashMap<>();
	
	public SessionServer(String Name) {
		super(Name);
	}
	
	public SessionServer(String Name, String Description) {
		super(Name, Description);
	}
	
	public void addClient(Client client) {
		ListClients.put(client.getName(), client);
	}
}
