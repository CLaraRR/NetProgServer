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
import java.util.Iterator;
import java.util.Scanner;

public class ClientService {
	ByteBuffer writeBuffer = ByteBuffer.allocate(1024);
	ByteBuffer readBuffer = ByteBuffer.allocate(1024);
	SelectionKey key;
	String address;
	int port;
	Selector selector;
	SocketChannel socketChannel;
	Iterator<SelectionKey> keyIterator;
	
	
	public ClientService(String address, int port) {
		super();
		this.address = address;
		this.port = port;
	}

	
	public ClientService() {
		super();
	}


	public boolean start(String address, int port) throws IOException
	{
		socketChannel=ClientSocketChannel.getSocketChannel(address, port);
		selector = Selector.open();
		// ע�����ӷ�����socket�Ķ���
		socketChannel.register(selector, SelectionKey.OP_CONNECT);
		// ѡ��һ���������Ӧ��ͨ����Ϊ I/O ����׼��������
		// �˷���ִ�д�������ģʽ��ѡ�������
		selector.select();
		// ���ش�ѡ��������ѡ�������
		 keyIterator = selector.selectedKeys().iterator();
		 if(keyIterator.hasNext())
		{
			 key = keyIterator.next();
		     keyIterator.remove();
		}
		// �жϴ�ͨ�����Ƿ����ڽ������Ӳ�����
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
	
	public String send(String data) {
		
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
		// ������������־��λ,��Ϊ������put�����ݱ�־���ı�Ҫ����ж�ȡ���ݷ��������,��Ҫ��λ
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
			// ������������Ա��´ζ�ȡ
			readBuffer.clear();
			int num = client.read(readBuffer);
			backMessage=new String(readBuffer.array(), 0, num);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return backMessage;
		// ע�����������һ�ζ�ȡ
	}
	
}
