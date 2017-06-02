package service;

import java.util.Date;

public interface Service {

	 String login(String username,String password);
	 
	 String register(String username,String password,int sex,String brith);
	 
	 String set(String username,int userID,String password,int sex,String brith,int deciveID);
	 
	 String sendMessageToPublic(String message,String time);
	 
	 String sendMessageToOne(String receiveName,String message);
	 
	 String addFriend(String userName);
	 
	 String reflashData();
	 String reflashCommnuity();
	 String reflashFriend();
	 String reflashSet();
	 
	 
}
