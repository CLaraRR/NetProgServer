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
	 * 请求的转发
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
	    //在此处添加方法名
	    case "LH": registered(parameter);break;
	    }
	}
	
	/**
	 * 获得对应的方法名
	 * @param key
	 * @return 方法名
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
	         System.out.println("服务端收到信息："+message);  
//	         ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());  
//	         channel.write(outBuffer);// 将消息回送给客户端  
	         return message;
	}
	
	/**
	 * 用户注册
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
	 * 发送消息
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
			System.out.println("消息发出");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
