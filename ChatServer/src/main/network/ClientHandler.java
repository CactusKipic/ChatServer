package main.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

import com.google.gson.Gson;

import main.Message;
import main.SessionServer;
import main.core.Client;

public class ClientHandler implements Runnable{
	
	private Socket socket;
	private LinkedList<Message> messagePile = new LinkedList<>();
	private boolean readingPile = false;
	private BufferedReader reader;
	private SessionServer session;
	
	public ClientHandler(Socket socket) {
		this.socket = socket;
	}
	
	public void sendMessage(Message message) {
		System.out.println("Reception message pour diffusion");
		while(readingPile == true);
		messagePile.add(message);
	}
	
	
	@Override
	public void run() {
		System.out.print("Démarrage ClientHandler");
		PrintWriter writer;
		boolean endConnexion = false;
		Gson gson = new Gson();
		while(!socket.isClosed()) {
            
            try {
				writer = new PrintWriter(socket.getOutputStream());
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            
				//System.out.println("Attente...");
	            // Attente du Client
	            String requete = read();
				//System.out.println("Reçu");
	            System.out.println("REQ: "+requete);
	            
	            // Traitement de la requête du client
	            String reponse = "";
	            
	            switch(requete.substring(0, 5).toUpperCase()){
	               case "STDBY":
	            	   System.out.println("Standing by.");
	            	   readingPile = true;
	            	   if(messagePile.isEmpty())
	            		   reponse = "STDBY";
	            	   else {
	            		   Message msg = messagePile.pop();
	            		   reponse = (messagePile.isEmpty() ? "LAMSG" : "MDMSG") +gson.toJson(msg);
	            	   }
	            	   readingPile = false;
	                  break;
	               case "NXMSG":
	            	   readingPile = true;
            		   Message msg = messagePile.pop();
            		   reponse = (messagePile.isEmpty() ? "LAMSG" : "MDMSG") +gson.toJson(msg);
	            	   readingPile = false;
	            	   break;
	               case "MSGSD":
	            	   if(session != null) {
	            		   Message message = gson.fromJson(requete.substring(5), Message.class);
	            		   session.NewMessage(message);
	            		   reponse = "ACKMS";
	            	   }
	            	   break;
	               case "CTION":
	            	   String str = requete.substring(5);
	            	   System.out.println("Split: "+str.split("\\|")[0]+" "+str.split("\\|")[1]);
	            	   String[] list = str.split("\\|");
            		   System.out.println("Connexion à une session...");
	            	   session = SessionServer.getSession(list[0]);
	            	   if(session == null) {
	            		   System.out.println("Création de la session");
	            		   session = SessionServer.createSession(list[0]);
	            	   }
            		   System.out.println("Succès ? "+session!=null);
	            	   if(session.getClient(list[1]) == null) {
	            		   System.out.println("Session: "+session.getSessionName());
		            	   session.addClient(new Client(list[1],this));
		            	   reponse = "ACKCO";
	            	   }else {
	            		   System.out.println("Erreur lors de la création ou client pseudo déjà utilisé");
	            		   reponse = "REFCO";
	            	   }
	            	   break;
	               case "ENDCO":
	            	   reponse = "ENDCO"; 
	            	   endConnexion = true;
	            	   break;
	               default: 
	            	   reponse = "UNKWN";                     
	            	   break;
	            }
	            
	            // Envoie réponse
	            writer.write(reponse+"\r");
	            writer.flush();
	            
	            if(endConnexion){
	            	System.out.println("Fermeture connexion.");
	            	socket.close();
	            	break;
	            }        
			} catch (IOException e) {
				e.printStackTrace();
				break;
			}
		}
		
		
	}
	
   private String read() throws IOException{      
       String res = "";
       while((res = reader.readLine())==null);
      return res;
   }

}
