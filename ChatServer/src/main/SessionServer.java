package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;

import main.core.Client;

public class SessionServer extends Session{
	
	private static HashMap<String, SessionServer> Sessions = new HashMap<>();
	
	private transient HashMap<String, Client> ListClients = new HashMap<>();
	
	private SessionServer(String Name) {
		super(Name);
		ListClients = new HashMap<>();
		Sessions.put(this.getSessionName(), this);
		File f = new File(Main.PATH + Name +"/session.json");
		if(!f.getParentFile().exists())
			f.getParentFile().mkdirs();
		Gson gson = new Gson();
		String json = gson.toJson(this);
		try {
			PrintWriter PW = new PrintWriter(f,"UTF-8");
			PW.print(json);
			PW.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	private SessionServer(String Name, String Description) {
		super(Name, Description);
		ListClients = new HashMap<>();
		Sessions.put(this.getSessionName(), this);
		File f = new File(Main.PATH + Name +"/session.json");
		if(!f.getParentFile().exists())
			f.getParentFile().mkdirs();
		Gson gson = new Gson();
		String json = gson.toJson(this);
		try {
			PrintWriter PW = new PrintWriter(f,"UTF-8");
			PW.print(json);
			PW.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	
	public static SessionServer createSession(String Name, String Description) {
		if(SessionNameAvailable(Name))
			return new SessionServer(Name, Description);
		return null;
	}
	
	public static SessionServer createSession(String Name) {
		if(SessionNameAvailable(Name))
			return new SessionServer(Name, "");
		return null;
	}
	
	public static SessionServer getSession(String Name) {
		SessionServer session;
		if((session = Sessions.get(Name))!= null)
			return session;
		
		// Si la session n'est pas déjà chargée, on cherche à la charger depuis son fichier
		File f = new File(Main.PATH + Name +"/session.json");
		Gson gson = new Gson();
		String json;
		if(!f.exists())
			return null;
		
	    try {
			json = new String(Files.readAllBytes(f.toPath()));
			session = (SessionServer) gson.fromJson(json, SessionServer.class);
			session.ListClients = new HashMap<>();
			Sessions.put(session.getSessionName(), session);
			return session;
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
	
	public static boolean SessionNameAvailable(String name) {
		File f = new File(Main.PATH + name +"/session.json");
		return !f.exists();
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
	
	public void NewMessage(Message message) {
		SaveMessage(message);
		System.out.println("NB Clients: "+ListClients.size());
		for(Client client:ListClients.values()) {
			client.getClientHandler().sendMessage(message);
		}
	}
	
	public void SaveMessage(Message message) {
		File f = new File(Main.PATH + this.getSessionName() +"/"+message.getDate()+".messages");
		if(!f.exists())
			f.getParentFile().mkdirs();
		Gson gson = new Gson();
		String format = gson.toJson(message)+"\n";
		
		try {
			Files.write(f.toPath(), format.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
