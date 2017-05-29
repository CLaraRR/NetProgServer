package service;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * 
 * @author jy_meng
 *
 */

import dao.UserDao;
public class ServerService {
	UserDao userDao=new UserDao();
	SelectionKey key=null;

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
	    case "LH": registered(parameter);break;
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
		if(userDao.registered(usrname, password)==1)
		{
			sendMessage("+OK LOGIN");
		}
		else
		{
			sendMessage("+ERROR LOGIN");
		}
		
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
