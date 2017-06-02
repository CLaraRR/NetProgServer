  package start;  
      
    import java.io.IOException;  
    import java.net.InetSocketAddress;  
    import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;  
    import java.nio.channels.Selector;  
    import java.nio.channels.ServerSocketChannel;  
    import java.nio.channels.SocketChannel;  
    import java.util.Iterator;

import service.ServerService;  
       
    /** 
     * NIO����� 
     * @author jy_meng
     */  
    public class Server {  
        //ͨ��������  
        private Selector selector;
        private ServerService serverService;
      
        /** 
         * ���һ��ServerSocketͨ�������Ը�ͨ����һЩ��ʼ���Ĺ��� 
         * @param port  �󶨵Ķ˿ں� 
         * @throws IOException 
         */  
        public void initServer(int port) throws IOException {  
        	
        	serverService=new ServerService();
            // ���һ��ServerSocketͨ��  
            ServerSocketChannel serverChannel = ServerSocketChannel.open();  
            // ����ͨ��Ϊ������  
            serverChannel.configureBlocking(false);  
            // ����ͨ����Ӧ��ServerSocket�󶨵�port�˿�  
            serverChannel.socket().bind(new InetSocketAddress(port));  
            // ���һ��ͨ��������  
            this.selector = Selector.open();  
            //��ͨ���������͸�ͨ���󶨣���Ϊ��ͨ��ע��SelectionKey.OP_ACCEPT�¼�,ע����¼���  
            //�����¼�����ʱ��selector.select()�᷵�أ�������¼�û����selector.select()��һֱ������
            serverChannel.register(selector, SelectionKey.OP_ACCEPT);  
        }  
      
        /** 
         * ������ѯ�ķ�ʽ����selector���Ƿ�����Ҫ������¼�������У�����д��� 
         * @throws IOException 
         */  
        @SuppressWarnings("unchecked")  
        public void listen() throws IOException {  
            System.out.println("����������ɹ���");  
            // ��ѯ����selector  
            while (true) {  
                //��ע����¼�����ʱ���������أ�����,�÷�����һֱ����  
                selector.select();  
                // ���selector��ѡ�е���ĵ�������ѡ�е���Ϊע����¼�  
                Iterator ite = this.selector.selectedKeys().iterator();  
                while (ite.hasNext()) {
                	String a="";
                    SelectionKey key = (SelectionKey) ite.next();  
                    // ɾ����ѡ��key,�Է��ظ�����  
                     key.toString();
                    // �ͻ������������¼�  
                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key  
                                .channel();  
                        // ��úͿͻ������ӵ�ͨ��  
                        SocketChannel channel = server.accept(); 
                        System.out.println(channel.getRemoteAddress());
                        // ���óɷ�����  
                        channel.configureBlocking(false);  
      
                        //��������Ը��ͻ��˷�����ϢŶ  
//                        channel.write(ByteBuffer.wrap(new String("��� "+channel.getRemoteAddress()).getBytes()));  
                        //�ںͿͻ������ӳɹ�֮��Ϊ�˿��Խ��յ��ͻ��˵���Ϣ����Ҫ��ͨ�����ö���Ȩ�ޡ�  
                        channel.register(this.selector, SelectionKey.OP_READ|SelectionKey.OP_WRITE); 
                        // ����˿ɶ����¼�  
                    }  if (key.isReadable()) { 
                    	
                    	serverService.getMethod(key);
                    }  
//                    if (key.isWritable())
//                    {
//                    	SocketChannel channel=(SocketChannel)key.channel();
//                    	channel.write(ByteBuffer.wrap(new String(a).getBytes()));
//                    }
                    ite.remove();
      
                }  
      
            }  
        }  
 
          
        public static void main(String[] args) throws IOException {  
            Server server = new Server();  
            server.initServer(8000);  
            server.listen();  
        }  
      
} 