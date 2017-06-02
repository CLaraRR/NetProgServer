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
     * 获得一个Socket通道，并对该通道做一些初始化的工作 
     * @param ip 连接的服务器的ip 
     * @param port  连接的服务器的端口号          
     * @throws IOException 
     */  
    public void initClient() {  
  
        // 获得一个通道管理器  
        try {
			this.selector = Selector.open();
	        //将通道管理器和该通道绑定，并为该通道注册SelectionKey.OP_CONNECT事件。  
	        socketChannel.register(selector, SelectionKey.OP_CONNECT);  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
    } 
    /** 
     * 采用轮询的方式监听selector上是否有需要处理的事件，如果有，则进行处理 
     * @throws IOException 
     */  
    @SuppressWarnings("unchecked")  
    public void listen() throws IOException {  
        // 轮询访问selector  
        while (true) {  
        	
//        	select()阻塞到至少有一个通道在你注册的事件上就绪了。
            selector.select();
            //当像Selector注册Channel时，Channel.register()方法会返回一个SelectionKey 对象。
            //这个对象代表了注册到该Selector的通道。
            //可以通过SelectionKey的selectedKeySet()方法访问这些对象。 
            //获得selector中选中的项的迭代器  
            Iterator ite = this.selector.selectedKeys().iterator();  
            while (ite.hasNext()) {
                SelectionKey key = (SelectionKey) ite.next();  
                // 删除已选的key,以防重复处理  
             
                // 连接事件发生  
                if (key.isConnectable()) {  
                    SocketChannel channel = (SocketChannel) key  
                            .channel();  
                    // 如果正在连接，则完成连接  
                    if(channel.isConnectionPending()){  
                        channel.finishConnect(); 
                    }  
                    // 设置成非阻塞  
                    channel.configureBlocking(false);  
  
                    //在这里可以给服务端发送信息哦  
//                   channel.write(ByteBuffer.wrap(new String("LH test 123456 LD").getBytes()));  
                    //在和服务端连接成功之后，为了可以接收到服务端的信息，需要给通道设置读的权限。  
                    channel.register(this.selector, SelectionKey.OP_READ);  
                      
                    // 获得了可读的事件  
                } else 
                	if (key.isReadable()) { 
                        read(key);  
                }  
                 ite.remove();  
  
            }  
  
        }  
    }  
    /** 
     * 处理读取服务端发来的信息 的事件 
     * @param key 
     * @throws IOException  
     */  
    public void read(SelectionKey key) throws IOException{  
        // 服务器可读取消息:得到事件发生的Socket通道  
        SocketChannel channel = (SocketChannel) key.channel();  
        // 创建读取的缓冲区  
        ByteBuffer buffer = ByteBuffer.allocate(1024);  
        channel.read(buffer);  
        byte[] data = buffer.array();  
        String msg = new String(data).trim();  
        System.out.println(Thread.currentThread().getName()+"收到消息："+msg);
//        key.channel().close();
//        ByteBuffer outBuffer = ByteBuffer.wrap(msg.getBytes());  
//        channel.write(outBuffer);// 将消息回送给客户端  
    }  
      
	
}
