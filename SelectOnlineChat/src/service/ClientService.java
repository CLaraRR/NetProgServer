package service;

import java.io.DataOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;

public class ClientService implements Service {
	private  ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
	private ByteBuffer readBuffer = ByteBuffer.allocate(1024);
	private SelectionKey key;
	private String address;
	private int port;
	private String  userName;
    private Integer userId;
	private Selector selector;
	private SocketChannel socketChannel;
	private Iterator<SelectionKey> keyIterator;
	
	
	public ClientService(String address, int port) {
		super();
		this.address = address;
		this.port = port;
	}

	
	public ClientService() {
		super();
	}


	private boolean start(String address, int port) throws IOException
	{
		socketChannel=ClientSocketChannel.getSocketChannel(address, port);
		selector = Selector.open();
		// 注册连接服务器socket的动作
		socketChannel.register(selector, SelectionKey.OP_CONNECT);
		// 选择一组键，其相应的通道已为 I/O 操作准备就绪。
		// 此方法执行处于阻塞模式的选择操作。
		selector.select();
		// 返回此选择器的已选择键集。
		 keyIterator = selector.selectedKeys().iterator();
		 if(keyIterator.hasNext())
		{
			 key = keyIterator.next();
		     keyIterator.remove();
		}
		// 判断此通道上是否正在进行连接操作。
		if (key.isConnectable()) {
			try {
				socketChannel.finishConnect();
			} catch (Exception e) {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}
	
	private String send(String data) {
		if(socketChannel==null)
			{
			try {
				start("localhost", 8000);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}}
		String message = data;
		String backMessage="";
		writeBuffer.clear();
		writeBuffer.put(message.getBytes());
		// 将缓冲区各标志复位,因为向里面put了数据标志被改变要想从中读取数据发向服务器,就要复位
		writeBuffer.flip();
		try {
			socketChannel.write(writeBuffer);
			socketChannel.register(selector, SelectionKey.OP_READ);
			selector.select();
			keyIterator = selector.selectedKeys().iterator();
			key = keyIterator.next();
			keyIterator.remove();
//			System.out.print("receive message:");
			SocketChannel client = (SocketChannel) key.channel();
			// 将缓冲区清空以备下次读取
			readBuffer.clear();
			int num = client.read(readBuffer);
			backMessage=new String(readBuffer.array(), 0, num);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return backMessage;
		// 注册读操作，下一次读取
	}

	/**
	 * 注册
	 */
	@Override
	public String login(String username, String password) {
		String message=send("LH "+username+" "+password+" LT");
		this.userName=username;
		if(message!="+ERRORLOGIN")
		{
			String[] line=message.split("#");
			String[] lineToLine=line[1].split(" ");
			this.userId=Integer.valueOf(lineToLine[1]);
		}
		return message;
	}


	/**
	 * 登陆
	 */
	@Override
	public String register(String username, String password, int sex, String brith) {
		String message=send("GH "+username+" "+password+" "+sex+" "+brith+" GT");
		return message;
	}


	/**
	 * 设置
	 */
	@Override
	public String set(String username, int userID, String password, int sex, String brith, int deciveID) {
		String message=send("SH "+username+" "+userID+" "+password+" "+sex+" "+brith+" "+deciveID+" ST");
		return message;
	}


	@Override
	public String sendMessageToPublic(String message,String time) {
		String message_1=send("CH "+message+" "+userName+" "+time+" CT");
		return message_1;
	}


	@Override
	public String sendMessageToOne(String receiveName, String message) {
		String message_1=send("TH "+receiveName+" "+message+" TT");
		return message_1;
	}


	@Override
	public String addFriend(String userName) {
		String message=send("AH "+this.userName+' '+userName+" AT");
		return message;
	}


	@Override
	public String reflashData() {
		String message=send("RH DATA "+userId+" RT");
		return message;
	}


	public String getUserName() {
		return userName;
	}


	public void setUserName(String userName) {
		this.userName = userName;
	}


	public Integer getUserId() {
		return userId;
	}


	public void setUserId(Integer userId) {
		this.userId = userId;
	}


	@Override
	public String reflashCommnuity() {
		// TODO Auto-generated method stub
		String message=send("RH COMMNUITY "+userId+" RT");
		return message;
	}


	@Override
	public String reflashFriend() {
		// TODO Auto-generated method stub
		String message=send("RH FRIEND "+userId+" RT");
		return message;
	}


	@Override
	public String reflashSet() {
		// TODO Auto-generated method stub
		String message=send("RH SET "+userId+" RT");
		return message;
	}
	

	
	
}
