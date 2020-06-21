package main.network;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class ServerChat {
	
	private static int port = 12345;
	private static String ip = "127.0.0.1";
	private static ServerSocket server = null;
	private static boolean running = true;
	
	public static void launch() {

	      try {
	         server = new ServerSocket(port, 100, InetAddress.getByName(ip));
	      } catch (UnknownHostException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      }
	      
	      Thread t = new Thread(new Runnable(){
	         public void run(){
	            while(running == true){
	               
	               try {
	                  // Attente de la connexion d'un client
	                  Socket client = server.accept();
	                  
	                  System.out.println("Début de connexion: "+client.getInetAddress()+":"+client.getLocalPort());                  
	                  Thread t = new Thread(new ClientHandler(client));
	                  t.start();
	                  
	               } catch (IOException e) {
	                  e.printStackTrace();
	               }
	            }
	            
	            try {
	               server.close();
	            } catch (IOException e) {
	               e.printStackTrace();
	               server = null;
	            }
	         }
	      });
	      
	      t.start();
	}
	
	
}
