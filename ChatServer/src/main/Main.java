package main;

import java.io.File;

public class Main {
	
	public static final String PATH = "./Sessions/";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println("Démarrage du serveur de Chat");
		
		File f = new File(PATH);
		if(!f.exists()) {
			f.mkdirs();
		}
		
		
	}

}
