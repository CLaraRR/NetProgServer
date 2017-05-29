package service;

import java.io.IOException;

public class Test {
	static ClientService clientService=new ClientService("localhost",8000);
	public static void main(String[] args) {
		String message=clientService.send("LH 123 23");
		System.out.println(message);
		System.out.println(clientService.send("LH 123 23"));
	}

}
