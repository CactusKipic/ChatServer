package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	
	public void deleteClient(String name) {
		ListClients.remove(name);
	}
	
	public Client getClient(String name) {
		return ListClients.get(name);
	}
	
	public List<Client> getClients() {
		return new ArrayList<Client>(ListClients.values());
	}
}
