package server;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class SMain {
	
	public static void main(String[] args) throws SecurityException, IOException {	
		try {
			Server server = new Server();
			Naming.rebind("Server", server);
			
			System.out.println("Server is ready!!!");
		
			//data bind 비어있음
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
