package squidwars.host;

public class Chat {
	private String sender;
	private String message;
	public Chat(String sender, String message){
		this.sender = sender;
		this.message = message;
	}
	@Override
	public String toString() {
		String retval=this.sender+"<#>"+this.message;
		return retval;
	}
}
