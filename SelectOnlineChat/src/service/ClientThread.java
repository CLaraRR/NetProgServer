package service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class ClientThread extends Thread {
	private SocketChannel socketChannel;
	private Selector selector;  
	private String address;
	private int port;
	public ClientThread(String address, int port) {
		super();
		this.address = address;
		this.port = port;
		try {
			socketChannel=SocketChannel.open();
			socketChannel.configureBlocking(false);
			socketChannel.connect(new InetSocketAddress(address, port));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void run() {
		initClient();
		try {
			listen();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 /** 
     * ���һ��Socketͨ�������Ը�ͨ����һЩ��ʼ���Ĺ��� 
     * @param ip ���ӵķ�������ip 
     * @param port  ���ӵķ������Ķ˿ں�          
     * @throws IOException 
     */  
    public void initClient() {  
  
        // ���һ��ͨ��������  
        try {
			this.selector = Selector.open();
	        //��ͨ���������͸�ͨ���󶨣���Ϊ��ͨ��ע��SelectionKey.OP_CONNECT�¼���  
	        socketChannel.register(selector, SelectionKey.OP_CONNECT);  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    } 
    /** 
     * ������ѯ�ķ�ʽ����selector���Ƿ�����Ҫ������¼�������У�����д��� 
     * @throws IOException 
     */  
    @SuppressWarnings("unchecked")  
    public void listen() throws IOException {  
        // ��ѯ����selector  
        while (true) {  
        	
//        	select()������������һ��ͨ������ע����¼��Ͼ����ˡ�
            selector.select();
            //����Selectorע��Channelʱ��Channel.register()�����᷵��һ��SelectionKey ����
            //������������ע�ᵽ��Selector��ͨ����
            //����ͨ��SelectionKey��selectedKeySet()����������Щ���� 
            //���selector��ѡ�е���ĵ�����  
            Iterator ite = this.selector.selectedKeys().iterator();  
            while (ite.hasNext()) {
                SelectionKey key = (SelectionKey) ite.next();  
                // ɾ����ѡ��key,�Է��ظ�����  
             
                // �����¼�����  
                if (key.isConnectable()) {  
                    SocketChannel channel = (SocketChannel) key  
                            .channel();  
                    // ����������ӣ����������  
                    if(channel.isConnectionPending()){  
                        channel.finishConnect(); 
                    }  
                    // ���óɷ�����  
                    channel.configureBlocking(false);  
  
                    //��������Ը�����˷�����ϢŶ  
//                   channel.write(ByteBuffer.wrap(new String("LH test 123456 LD").getBytes()));  
                    //�ںͷ�������ӳɹ�֮��Ϊ�˿��Խ��յ�����˵���Ϣ����Ҫ��ͨ�����ö���Ȩ�ޡ�  
                    channel.register(this.selector, SelectionKey.OP_READ);  
                      
                    // ����˿ɶ����¼�  
                } else 
                	if (key.isReadable()) { 
                        read(key);  
                }  
                 ite.remove();  
  
            }  
  
        }  
    }  
    /** 
     * �����ȡ����˷�������Ϣ ���¼� 
     * @param key 
     * @throws IOException  
     */  
    public void read(SelectionKey key) throws IOException{  
        // �������ɶ�ȡ��Ϣ:�õ��¼�������Socketͨ��  
        SocketChannel channel = (SocketChannel) key.channel();  
        // ������ȡ�Ļ�����  
        ByteBuffer buffer = ByteBuffer.allocate(1024);  
        channel.read(buffer);  
        byte[] data = buffer.array();  
        String msg = new String(data).trim();  
        System.out.println(Thread.currentThread().getName()+"�յ���Ϣ��"+msg);
//        key.channel().close();
//        ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());  
//        channel.write(outBuffer);// ����Ϣ���͸��ͻ���  
    }  
      
	
}
