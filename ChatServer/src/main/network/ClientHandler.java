package main.network;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;

import main.Message;
import main.SessionServer;
import main.core.Client;

public class ClientHandler implements Runnable{
	
	private Socket socket;
	private LinkedList<Message> messagePile = new LinkedList<>();
	private boolean readingPile = false;
	private BufferedInputStream reader;
	private SessionServer session;
	
	public ClientHandler(Socket socket) {
		this.socket = socket;
	}
	
	public void sendMessage(Message message) {
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
				reader = new BufferedInputStream(socket.getInputStream());
	            
	            // Attente du Client
	            String requete = read();
	            
	            // Traitement de la requête du client
	            String reponse = "";
	            
	            switch(requete.substring(0, 4).toUpperCase()){
	               case "STDBY":
	            	   readingPile = true;
	            	   if(messagePile.isEmpty())
	            		   reponse = "STDBY";
	            	   else {
	            		   Message msg = messagePile.pop();
	            		   reponse = (messagePile.isEmpty() ? "MDMSG" : "LAMSG") +gson.toJson(msg);
	            	   }
	            	   readingPile = false;
	                  break;
	               case "NXMSG":
	            	   readingPile = true;
            		   Message msg = messagePile.pop();
            		   reponse = (messagePile.isEmpty() ? "MDMSG" : "LAMSG") +gson.toJson(msg);
	            	   readingPile = false;
	            	   break;
	               case "MSGSD":
	            	   if(session != null) {
	            		   Message message = gson.fromJson(requete.substring(4), Message.class);
	            		   session.NewMessage(message);
	            		   reponse = "ACKMS";
	            	   }
	            	   break;
	               case "CTION":
	            	   String str = requete.substring(4);
	            	   List<String> list = Arrays.asList(str.split("|"));
	            	   session = SessionServer.getSession(list.get(0));
	            	   if(session == null) {
	            		   session = SessionServer.createSession(list.get(0));
	            	   }
	            	   if(session.getClient(list.get(1))==null) {
		            	   session.addClient(new Client(list.get(1),this));
		            	   reponse = "ACKCO";
	            	   }else {
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
	            writer.write(reponse);
	            writer.flush();
	            
	            if(endConnexion){
	            	System.out.println("Fermeture connexion.");
	            	socket.close();
	            	break;
	            }        
			} catch (IOException e) {
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
