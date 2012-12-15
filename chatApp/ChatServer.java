/**
 * Masih Azad absurdmasih@gmail.com
 * 
 * This is a simple chat program, written as a project for a university course.
 * It uses SWT for making visual things (Windowing and so on)
 * Surely parts of this code has been copied from sites like Stackoverflow.com and so on.
 * Unfortunately I have forgot to mention them. now they are changed and it's hard to
 * find out where they were taken from.
 * 
 * since this program uses SWT, it's expected to run flawlessly in Eclipse
 * but without Eclipse, the SWT library and dependencies should be somehow included.
 * 
 * When the program starts, listens on port 4444 and opens a window.
 * IP and Port of each client connected to this server will be shown in a list box
 * When one of these items is double clicked, a new window will be opened so
 * Chatting with that client is made possible.
 * 
 * It's so simple and full of not properly handled exceptions.  
 * 
 * I'll be happy if this piece of code could help someone.
 * no need to even mention my name (Free to copy or doing anything else)
 */

package chatApp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class Address {
	public String ip;
	public int port;
	public Address(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
}

class Message {
	String message;
	String ip;
	int port;
	public Message(String message, String ip, int port) {
		this.ip = ip;
		this.port = port;
		this.message = message;
	}
}

public class ChatServer {
	public static void main(String args[]) {
		int PORT=4444;
		BlockingQueue<Address> addressing = new LinkedBlockingQueue<Address>();
		List<Socket> addresses = new ArrayList<Socket>();
	    ServerSocket server = null;
	    BlockingQueue<Message> q =
                new LinkedBlockingQueue<Message>();

		if (args.length>0) {
			try {
				int temp = Integer.parseInt(args[0]);
				if (temp>1024 && temp<6535) {
					PORT=temp;
				} else {
					PORT = 4444;
				}
				
			} catch (Exception e) {
				PORT=4444;
			}
		}
		try {
			server = new ServerSocket(PORT);
		} catch (IOException e) {
			e.printStackTrace();
		}

        Thread m = new Thread(new ServerThread(server,PORT, q, addresses, addressing));
        m.start();
        Thread win1 = new Thread(new Windowing(server, q, addresses, addressing));
        win1.start();
	}
	

}