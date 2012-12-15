package chatApp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class Windowing implements Runnable{

	final ServerSocket server;
	private final BlockingQueue<Message> queue;
	private final BlockingQueue<Address> addressing;
	private final List<Socket> addresses;
	protected static Shell shell;
    static StyledText text;
    static Text text1;
    static Display display;
    static org.eclipse.swt.widgets.List list;
	
	public Windowing(ServerSocket server, BlockingQueue<Message> q, List<Socket> addr, BlockingQueue<Address> ad) { 
		this.addressing = ad;
		this.server = server;
		queue = q;
		addresses = addr;
		}
	
    
    private void win(int i) {
        Thread win1 = new Thread(new SubWindowing(addresses.get(i), queue, shell));
        win1.start();
    }
	
    protected  void addList() {
		Address temp = null;
		try {
			System.out.println("Addressing is not empty");
			temp = addressing.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		list.add(temp.ip+":"+temp.port);
		shell.redraw();
		System.out.println("List is  appended");
	}
    
    @Override
    public void run() {
    	display = new Display();
    	createContents();
        shell.open();
        while (!shell.isDisposed()) {
        	if (!display.readAndDispatch()) {
        		if (!addressing.isEmpty()) {
	        		addList();
        		}
        		shell.redraw();
        		display.sleep();
        	}
        }
        display.dispose();
    }
    
    public void createContents() {
    	shell = new Shell(display, SWT.MIN);
        shell.setText("Simple Java Server Chat by masihazad");
        shell.setSize(550, 590);
        shell.addDisposeListener(new DisposeListener() {    
            @Override
            public void widgetDisposed(DisposeEvent e) {
                System.out.println("It's over");
                try {
                	for (Socket sock : addresses) {
						sock.close();
					}
					server.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
                System.exit(0);
            }
        });
        
        text = new StyledText(shell, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
        text.setLayoutData(new GridData(GridData.FILL_BOTH));
        text.setEnabled(false);
        text.setBounds(10, 10, 400, 500);
        text.setVisible(true);

        list = new org.eclipse.swt.widgets.List(shell, SWT.BORDER);
		list.setBounds(420, 10, 100, 500);
		list.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				// TODO Auto-generated method stub
				int i = list.getSelectionIndex();
				win(i);
				shell.redraw();
			}
		});                        
      }

}