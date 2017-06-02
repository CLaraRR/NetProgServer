package start;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import service.ClientService;
import service.ClientThread;
import service.Service;

public class Client {
	
	public static void main(String[] args) {
		
		Service service=new ClientService();
		String message=service.login("user1", "123456");
		System.out.println(message);
		message=service.reflashSet();
		System.out.println(message);
		ClientThread clientThread=new ClientThread("localhost", 8000);
		clientThread.start();
		
		
	}

}