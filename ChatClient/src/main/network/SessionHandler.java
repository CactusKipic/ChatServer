package main.network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.LinkedList;

import com.google.gson.Gson;

import main.Message;
import main.ui.ChatRoom;

public class SessionHandler implements Runnable{
	
	private Socket socket;
	private LinkedList<Message> messagePile = new LinkedList<>();
	private boolean readingPile = false;
	private BufferedInputStream reader;
	private String state = "CTION";
	private String sessionName;
	private String username;
	
	public SessionHandler(String ip, int port, String username, String sessionName) {
		this.sessionName = sessionName;
		this .username = username;
		try {
			socket = new Socket(ip,port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}

	public void sendMessage(Message message) {
		while(readingPile == true);
		messagePile.add(message);
	}
	
	
	@Override
	public void run() {
		System.out.print("D�marrage ClientHandler");
		PrintWriter writer;
		boolean endConnexion = false;
		Gson gson = new Gson();
		while(!socket.isClosed()) {
            
            try {
				writer = new PrintWriter(socket.getOutputStream());
				reader = new BufferedInputStream(socket.getInputStream());
	            // Choix et envoie de requ�te
	            String requete = "";
				
				switch(state) {
					case "STDBY":
						if(messagePile.isEmpty())
							requete = "STDBY";
						else {
		            	   readingPile = true;
	            		   Message msg = messagePile.pop();
	            		   requete = "MSGSD" +gson.toJson(msg);
		            	   readingPile = false;
						}
						break;
					case "CTION":
						requete = "CTION"+sessionName+"|"+username;
						break;
					case "WTMSG":
						requete = "NXMSG";
					case "ENDCO":
						requete = "ENDCO";
						break;
				}

	            
	            // Envoie requ�te
	            writer.write(requete);
	            writer.flush();
	            
	            // Attente de la r�ponse du serveur
	            String reponse = read();
	            Message message;
	            
	            switch(reponse.substring(0, 4).toUpperCase()){
	               case "STDBY":
	            	   System.out.println("Standing by.");
	            	   this.wait(100);
	                  break;
	               case "MDMSG":
            		   message = gson.fromJson(requete.substring(4), Message.class);
            		   ChatRoom.newMessage(message);
            		   state = "WTMSG";
	            	   break;
	               case "LAMSG":
            		   message = gson.fromJson(requete.substring(4), Message.class);
            		   ChatRoom.newMessage(message);
            		   state = "STDBY";
	            	   break;
	               case "ACKMS":
	            	   System.out.println("Confirmation de message re�u.");
	            	   state = "STDBY";
	            	   break;
	               case "ACKCO":
	            	   System.out.println("Connexion r�ussie.");
	            	   state = "STDBY";
	            	   break;
	               case "REFCO":
	            	   System.out.println("Connexion refus�e.");
	            	   state = "ENDCO";
	            	   break;
	               case "UNKWN":
	            	   if(state == "CTION") {
	            		   System.out.println("R�ponse inconnue du serveur lors de la tentative de connexion, arr�t de la connexion.");
	            		   state = "ENDCO";
	            	   }
	            	   else {
		            	   System.out.println("Erreur de communication avec le serveur, retour en STDBY.");
		            	   state = "STDBY";
	            	   }
	            	   break;
	               case "ENDCO":
	            	   System.out.println("Connexion termin�e avec succ�s");
	            	   endConnexion = true;
	            	   break;
	               default:
	            	   System.out.println("Unknown response from server.");
	            	   break;
	            }
	            
	            if(endConnexion){
	            	if(state == "ENDCO") {
		            	System.out.println("Connexion ferm�e.");
	            		break;
	            	}else {
		            	System.out.println("Fermeture connexion...");
		            	state = "ENDCO";
	            	}
	            }
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
   private String read() throws IOException{      
       String res = "";
       int stream;
       byte[] b = new byte[1024];
       while((stream = reader.read(b)) != -1){
          res += new String(b, 0, stream);
       }
      return res;
   }
	
	
	
	
	
}
