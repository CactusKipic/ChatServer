package main;

public abstract class Session {
	
	private final String Name;
	private String Description;
	
	protected Session(String Name) {
		this.Name = Name;
		this.Description = "";
	}
	
	protected Session(String Name, String Description) {
		this.Name = Name;
		this.Description = Description;
	}
	
	public String getSessionName() {
		return Name;
	}
	
	public String getSessionDescription() {
		return Description;
	}
	
	public void setDescription(String Description) {
		this.Description = Description;
	}
}
