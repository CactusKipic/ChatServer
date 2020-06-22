package main;

public class Message {
	
	private String Auteur;
	private String Message;
	private String Date;
	
	public Message(String Auteur, String Message, String Date) {
		this.Auteur = Auteur;
		this.Message = Message;
		this.Date = Date;
	}
	
	public String getMessage() {
		return Message;
	}
	
	public String getAuthor() {
		return Auteur;
	}
	
	public String getDate() {
		return Date;
	}
	
}
