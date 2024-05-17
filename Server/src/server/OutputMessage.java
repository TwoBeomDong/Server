package server;

import java.io.Serializable;

public class OutputMessage implements Serializable{
	private static final long serialVersionUID = 1L;
	private String string = null;
	private ServerMessage message = null;
	public OutputMessage(ServerMessage m) {
		message = m;
	}
	public OutputMessage(String s) {
		string = s;
	}
	public String getMessage() {
		if(string != null) return string;
		if(message != null) return message.getMessage();
		return null;
	}
}