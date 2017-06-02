package service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import dao.*;
import entities.*;

/**
 * 
 * @author jy_meng
 *
 */

import dao.UserDao;
public class ServerService {
	UserDao userDao=new UserDao();
	DeviceDao devicedao=new DeviceDao();
	DataDao datadao=new DataDao();
	MessageDao messagedao=new MessageDao();
	RelationDao relationdao=new RelationDao();
	SelectionKey key=null;
	static TreeMap<Integer,SelectionKey> list_online=new TreeMap();
	private ByteBuffer sendBuffer = ByteBuffer.allocate(1024);//��������Ĵ�С���Կ�����ӡ����ı仯 
	/**
	 * �����ת��
	 * @param key
	 * @throws IOException
	 */
	public  void getMethod(SelectionKey key) throws IOException
	{
		this.key=key;
	    String message=getMessage();
	    String method="";
	    String parameter="";
	    if(message!=null&&message.length()!=0)
	    {
	    	method=message.substring(0, message.indexOf(" "));
	    	parameter=message.substring( message.indexOf(" ")+1, message.length()-1);
	    }
	    switch(method)
	    {
	    //�ڴ˴���ӷ�����
	    case "GH": registered(parameter);break;//ע��
	    case "LH": login(parameter);break;//��½
	    case "SH": set(parameter);break;//����
	    case "NH": dataSave(parameter);break;//��������
	    case "CH": addmessage(parameter);break;//����������Ϣ
	    case "AH": addfriend(parameter);break;//��Ӻ���
	    case "RH": refresh(parameter);break;//ˢ��
	    case "TH": talk(parameter);break;//����
	    case "EH": connect(parameter,key);break;//����
	    }
	}
	
	/**
	 * ��ö�Ӧ�ķ�����
	 * @param key
	 * @return ������
	 * @throws IOException
	 */
	public String getMessage() throws IOException
	{
		 SocketChannel channel = (SocketChannel) key.channel();  
		  ByteBuffer buffer = ByteBuffer.allocate(1024);
	         String message = "";
	         try {
	        	 while(channel.read(buffer)>0)
	        	 {
	        		 buffer.flip();
	        		 message +=new String(buffer.array()).trim();
	        	 }
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				key.cancel();
				 if (key.channel() != null) {
					 key.channel().close();
	             }
			}  
	         System.out.println("������յ���Ϣ��"+message);  
//	         ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());  
//	         channel.write(outBuffer);// ����Ϣ���͸��ͻ���  
	         return message;
	}
	
	/**
	 * �û�ע��
	 * @param key
	 */
	public void registered(String parameter)
	{
		if(parameter.length()==0)return;
		String[] elements=parameter.split(" ");
		String usrname=elements[0];
		String password=elements[1];
		int sex=Integer.parseInt(elements[2]);
		String birth=elements[3];
		if(userDao.registered(usrname, password,sex,birth)==1)
		{
			sendMessage("+OKREGISTER");
		}
		else
		{
			sendMessage("+ERRORUSERNAM");
		}
		
	}
	
	public void login(String parameter)
	{
		if(parameter.length()==0)return;
		String[] elements=parameter.split(" ");
		String username=elements[0];
		String password=elements[1];
		String response;
		if(userDao.login(username, password)==1)
		{
			
			//��ӵ�½�ɹ���ĳ�ʼ��
			response="+OK LOGIN";
			int userId=userDao.getIdByName(username);
			response+="#UD "+userId;//�����û�ID
			//���ߺ����б�
			response+="#FH";
			List<User> friend=userDao.getFriendById(userId);
			for(int i=0;i<friend.size();i++)
			{
				if(list_online.get(friend.get(i).getUserid())!=null)
					response+=' '+friend.get(i).getUsername();
			}
			response+=" FT";
			//������
			response+="#DH";
			List<Data> data=datadao.GetData(username);
			for(int i=0;i<data.size()&&i<10;i++)
			{
				response+=" "+data.get(i).getDatacontent()+"*"+data.get(i).getTime();
			}
			response+=" DT";
			//������Ϣ
			response+="#CH";
			List<Message> message=messagedao.getAll();
			for(int i=0;i<message.size()&&i<10;i++)
			{
				response+=" "+message.get(i).getContent()+'*'+message.get(i).getUsername()+'*'+message.get(i).getTime();//?
			}
			response+=" CT";
			sendMessage(response);
		}
		else
		{
			sendMessage("+ERRORLOGIN");
		}
	}
	public void set(String parameter)//����
	{
		if(parameter.length()==0)return;
		String[] elements=parameter.split(" ");
		String username=elements[0];
		int  userid=Integer.parseInt(elements[1]);
		String password=elements[2];
		int sex=Integer.parseInt(elements[3]);
		String birth=elements[4];
		int deviceid=Integer.parseInt(elements[5]);
		int res=userDao.set(userid,username,password, sex, birth);
		devicedao.add(userid, deviceid);
		if(res==-1)
			sendMessage("+ERRORUSERNAME");
		else
			sendMessage("+OKSET");
		
	}
	public void dataSave(String parameter)//?????
	{
		if(parameter.length()==0)return;
		String[] elements=parameter.split(" ");
		String content=elements[1];
		String time=TimeStamp2Date(elements[2]);
		int deviceId=Integer.parseInt(elements[0]);
		int userid=devicedao.getIdByDevice(deviceId);
		datadao.addData(content, userid,time);
	}
	public static String TimeStamp2Date(String timestampString) {
        
        String formats = "yyyy-MM-dd HH:mm:ss";
        Long timestamp = Long.parseLong(timestampString) * 1000;
        String date = new SimpleDateFormat(formats, Locale.CHINA).format(new Date(timestamp));
        return date;
    }
	public void addfriend(String parameter)//��Ӻ���
	{
		if(parameter.length()==0)return;
		String[] elements=parameter.split(" ");
		String username1=elements[0];
		String username2=elements[1];
		int user1=userDao.getIdByName(username1);
		int user2=userDao.getIdByName(username2);
		if(user1==-1||user2==-1)
			sendMessage("+ERRORADD");
		else
		{
			relationdao.add(user1, user2, 0);
			sendMessage("+OKADD");
		}
	}
	
	public void addmessage(String parameter)//���������Ϣ
	{
		if(parameter.length()==0)return;
		String[] elements=parameter.split(" ");
		String date=elements[0];
		String username=elements[1];
		String content=elements[2];
		String time=elements[3];
		int userid=userDao.getIdByName(username);
		if(userid==-1)
			sendMessage("+ERRORSEND");//�û���������
		else
		{
			messagedao.addMessage(content, userid,time);
			sendMessage("+OKSEND");
		}
	}
	public void refresh(String parameter)//ˢ��
	{
		if(parameter.length()==0)return;
		String[] elements=parameter.split(" ");
		String method=elements[0];
		int userid=Integer.parseInt(elements[1]);
		String username=userDao.getNameById(userid);
		String response = "";
		switch(method)
		{
		case "SET":
			User u=new User();
			u=userDao.getById(userid);
			response+="SH "+u.getUsername()+' '+u.getUserid()+' '+u.getUserword()+' '+u.getSex()+' '+u.getBirth();
			int deviceid=devicedao.getDeviceById(userid);
			System.out.println("device is "+deviceid);
			response+=" "+deviceid+" ST";
			;break;
		case "DATA":
			response+="DH";
			List<Data> data=datadao.GetData(username);
			for(int i=0;i<data.size()&&i<10;i++)
			{
				response+=" "+data.get(i).getDatacontent()+"*"+data.get(i).getTime();
			}
			response+=" DT";;break;
		case "COMMNUITY":
			response+="CH";
			List<Message> message=messagedao.getAll();
			for(int i=0;i<message.size()&&i<10;i++)
			{
				response+=" "+message.get(i).getContent()+'*'+message.get(i).getUsername()+'*'+message.get(i).getTime();//?
			}
			response+=" CT";;break;
		case "FRIEND":
			response+="FH";
			List<User> friend=userDao.getFriendById(userid);
			for(int i=0;i<friend.size();i++)
			{
				if(list_online.get(friend.get(i).getUserid())!=null)
					response+=' '+friend.get(i).getUsername();
			}
			response+=" FT";;break;
		}
		sendMessage(response);
	}
	public void talk(String parameter) throws ClosedChannelException, IOException
	{
		if(parameter.length()==0)return;
		String[] elements=parameter.split(" ");
		String username=elements[0];
		String message=elements[1];
		int userid=userDao.getIdByName(username);
		SelectionKey key2=list_online.get(userid);
		write(key2, message);
	}
	 private void write(SelectionKey key,String message) throws IOException, ClosedChannelException {
	        SocketChannel channel = (SocketChannel) key.channel();
	        System.out.println("write:"+message);
	        sendBuffer.clear();
	        sendBuffer.put(message.getBytes());
	        sendBuffer.flip();
	        channel.write(sendBuffer);
	        
	    } 
	 public void connect(String parameter,SelectionKey key)
	 {
		 if(parameter.length()==0)return;
			String[] elements=parameter.split(" ");
		int userid=Integer.parseInt(elements[0]);
		list_online.put(userid, key);
		
	 }
	/**
	 * ������Ϣ
	 * @param message
	 */
	public void sendMessage(String message)
	{
		
		 SocketChannel channel = (SocketChannel) key.channel();  
		 ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
		 byteBuffer.clear();
		  try {
//			channel.write(ByteBuffer.wrap(new String(message).getBytes()));
			  byteBuffer.put(message.getBytes());
			  byteBuffer.flip();
			  channel.write(byteBuffer);
			System.out.println("��Ϣ����");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
