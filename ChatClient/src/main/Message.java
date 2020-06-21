package main;

public class Message {
	
	private String Author;
	private String Message;
	private String Date;
	
	public Message(String Author, String Message, String Date) {
		this.Author = Author;
		this.Message = Message;
		this.Date = Date;
	}
	
	public String getMessage() {
		return Message;
	}
	
	public String getAuthor() {
		return Author;
	}
	
	public String getDate() {
		return Date;
	}
	
}
