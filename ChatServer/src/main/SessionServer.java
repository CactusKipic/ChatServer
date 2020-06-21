package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.Gson;

import main.core.Client;

public class SessionServer extends Session{
	
	private static HashMap<String, SessionServer> Sessions = new HashMap<>();
	
	private HashMap<String, Client> ListClients = new HashMap<>();
	
	private SessionServer(String Name) {
		super(Name);
	}
	
	private SessionServer(String Name, String Description) {
		super(Name, Description);
		File f = new File(Main.PATH + Name +"/session.json");
		if(!f.getParentFile().exists())
			f.mkdirs();
		Gson gson = new Gson();
		String json = gson.toJson((Session)this);
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
		
	    try {
			json = new String(Files.readAllBytes(f.toPath()));
			return (SessionServer) gson.fromJson(json, Session.class);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
	
	public static boolean SessionNameAvailable(String name) {
		File f = new File(Main.PATH + name +"/");
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
		for(Client client:ListClients.values()) {
			client.getClientHandler().sendMessage(message);
		}
	}
	
	public void SaveMessage(Message message) {
		File f = new File(Main.PATH + this.getSessionName() +"/"+message.getDate()+".messages");
		if(!f.exists())
			f.mkdirs();
		
		String format = "\r"+message.getDate() + " | "+message.getAuthor()+":\r"+message.getMessage();
		
		PrintWriter PW;
		try {
			PW = new PrintWriter(f,"UTF-8");
			PW.print(format);
			PW.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
