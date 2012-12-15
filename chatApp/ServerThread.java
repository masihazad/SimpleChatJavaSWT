package chatApp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;


public class ServerThread implements Runnable {
    final List<Socket> addresses;
    private final BlockingQueue<Address> addressing;
    private final BlockingQueue<Message> queue;
    final int PORT;//SET NEW CONSTANT VARIABLE: PORT
    ServerSocket server; //SET PORT NUMBER
    public ServerThread(ServerSocket server, int PORT, BlockingQueue<Message> q, 
    		List<Socket> addr,BlockingQueue<Address> ad) {
    	this.addressing = ad;
    	this.server = server;
    	this.PORT = PORT;
    	addresses = addr;
    	queue = q;
    	}
        
    public void run() {
    	Socket s = null;
    	try 
        {
    		System.out.println("Waiting for clients...");
            while (!Thread.interrupted())
            {												
                s = server.accept();
                System.out.println("Client connected from " + s.getLocalAddress().getHostName());
                addresses.add(s);
                addressing.put(new Address(s.getLocalAddress().getHostName(),s.getPort()));
                Thread chat = new Thread(new ClientThread(s,queue, addresses));
                chat.start();
                Thread.sleep(100);
            }
            
        } 
        catch (Exception e) 
        {
        	try {
        		if (s.isConnected()) {
        			s.close();
        		}
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
            System.out.println("The connection is lost");//IF AN ERROR OCCURED THEN PRINT IT
                        e.printStackTrace();
        } 
    }
}
