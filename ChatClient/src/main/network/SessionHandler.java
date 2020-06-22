package main.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;

import com.google.gson.Gson;

import main.Message;
import main.ui.ChatRoom;

public class SessionHandler implements Runnable{
	
	private Socket socket;
	private LinkedList<Message> messagePile = new LinkedList<>();
	private boolean readingPile = false;
	private BufferedReader reader = null;
	private String state = "CTION";
	private String sessionName;
	private String username;
	
	public SessionHandler(String ip, int port, String username, String sessionName) {
		this.sessionName = sessionName;
		this.username = username;
		try {
			socket = new Socket(InetAddress.getByName(ip),port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	public void sendMessage(Message message) {
		System.out.println("Ajout message pour envoie");
		while(readingPile == true);
		messagePile.add(message);
	}
	
	
	@Override
	public void run() {
		System.out.print("Démarrage ClientHandler");
		PrintWriter writer = null;
		boolean endConnexion = false;
		Gson gson = new Gson();
		
		while(!socket.isClosed()) {
            
            try {
            	if(writer == null)
            		writer = new PrintWriter(socket.getOutputStream());
            	if(reader == null)
            		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				System.out.println("Tour.");
	            // Choix et envoie de requête
	            String requete = "";
				
				switch(state) {
					case "STDBY":
						System.out.println("STDBY.");
						if(messagePile.isEmpty())
							requete = "STDBY";
						else {
		            	   readingPile = true;
	            		   Message msg = messagePile.pop();
	            		   System.out.println("Message: "+msg.getAuthor()+msg.getDate()+msg.getMessage());
	            		   requete = "MSGSD" +gson.toJson(msg);
		            	   readingPile = false;
						}
						break;
					case "CTION":
						System.out.println("CTION.");
						requete = "CTION"+sessionName+"|"+username;
						break;
					case "WTMSG":
						System.out.println("WTMSG.");
						requete = "NXMSG";
						break;
					case "ENDCO":
						System.out.println("ENDCO.");
						requete = "ENDCO";
						break;
				}
				System.out.println("REQ: "+ requete);
	            
	            // Envoie requête
	            writer.write(requete+"\r");
	            writer.flush();
	            
	            // Attente de la réponse du serveur
	            String reponse = read();
	            Message message;
	            System.out.println("RES: "+reponse);
	            switch(reponse.substring(0, 5).toUpperCase()){
	               case "STDBY":
	            	   System.out.println("Standing by.");
	            	   Thread.sleep(100);
	                  break;
	               case "MDMSG":
            		   message = gson.fromJson(reponse.substring(5), Message.class);
            		   ChatRoom.newMessage(message);
            		   state = "WTMSG";
	            	   break;
	               case "LAMSG":
            		   message = gson.fromJson(reponse.substring(5), Message.class);
            		   ChatRoom.newMessage(message);
            		   state = "STDBY";
	            	   break;
	               case "ACKMS":
	            	   System.out.println("Confirmation de message reçu.");
	            	   state = "STDBY";
	            	   break;
	               case "ACKCO":
	            	   System.out.println("Connexion réussie.");
	            	   state = "STDBY";
	            	   break;
	               case "REFCO":
	            	   System.out.println("Connexion refusée.");
	            	   state = "ENDCO";
	            	   break;
	               case "UNKWN":
	            	   if(state == "CTION") {
	            		   System.out.println("Réponse inconnue du serveur lors de la tentative de connexion, arrêt de la connexion.");
	            		   state = "ENDCO";
	            	   }
	            	   else {
		            	   System.out.println("Erreur de communication avec le serveur, retour en STDBY.");
		            	   state = "STDBY";
	            	   }
	            	   break;
	               case "ENDCO":
	            	   System.out.println("Connexion terminée avec succès");
	            	   endConnexion = true;
	            	   break;
	               default:
	            	   System.out.println("Unknown response from server.");
	            	   break;
	            }
	            
	            if(endConnexion){
	            	if(state == "ENDCO") {
		            	System.out.println("Connexion fermée.");
	            		break;
	            	}else {
		            	System.out.println("Fermeture connexion...");
		            	state = "ENDCO";
	            	}
	            }
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}
		System.out.println("Fin boucle");
		
	}
	
	public String getUsername() {
		return username;
	}
	
   private String read() throws IOException{      
       String res = "";
       while((res = reader.readLine())==null);
      return res;
   }
	
	
	
	
	
}
