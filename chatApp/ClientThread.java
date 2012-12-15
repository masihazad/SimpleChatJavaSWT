package chatApp;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

import org.eclipse.swt.widgets.Text;


class ClientThread implements Runnable{
	private final BlockingQueue<Message> queue;
	final List<Socket> addresses;
    private Socket socket;
    public Text text;
    
    public ClientThread(Socket s,BlockingQueue<Message> q, List<Socket> addr)
    {
    	queue = q;
        socket = s;
        addresses = addr;
    }
    
    @Override
    public void run() 
    {
    	try 
        {
            Scanner in = new Scanner(socket.getInputStream());
//            PrintWriter out = new PrintWriter(socket.getOutputStream());
            
            while (!Thread.interrupted())
            {		
                if (in.hasNext())
                {   
                    String input = in.nextLine();
                    queue.put(new Message(input,socket.getLocalAddress().toString(), socket.getPort()));
                }
                Thread.sleep(10);       
            }
        } catch (Exception e)
        {
        	try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
        	System.out.println(socket.getLocalAddress()+" : "+socket.getPort()+"Crached");
            e.printStackTrace();
        }	
    }
}
