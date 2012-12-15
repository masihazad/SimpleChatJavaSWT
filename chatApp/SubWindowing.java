package chatApp;

import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class SubWindowing implements Runnable {
	
    final Socket server;
	private final BlockingQueue<Message> queue;
	private  Shell parentShell;
	private  Shell shell;
    private StyledText text;
    private Text text1;
    private Display display;
    private org.eclipse.swt.widgets.List list;
    Windowing win;
	
	SubWindowing(Socket server, BlockingQueue<Message> q, Shell shel) { 
		this.server = server;
		queue = q;
		this.parentShell = shel;
		}
	
    
    
    @Override
    public void run() {
    	createContents();
        shell.open();
        Message tmp;
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
            	this.display.wake();
            	if (!queue.isEmpty()) {
            		try {
            			tmp = queue.peek();
            			if(tmp.ip.equals(server.getLocalAddress().toString()) && tmp.port == server.getPort()) {
            					text.append(tmp.ip + ":" + tmp.port + " >> "+ queue.take().message + "\n");
            			}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
            	}
            shell.redraw();
            display.sleep();
            }
        }
        display.dispose();
    }
    
    
    public void createContents() {
    	display =  new Display();
    	System.out.println("Executed");
    	shell = new Shell(display, SWT.MIN);
    	shell.setParent(parentShell);
        shell.setText("Chat with: " + server.getLocalAddress() + ":" + server.getPort());
        shell.setSize(550, 590);
        shell.addDisposeListener(new DisposeListener() {    
            @Override
            public void widgetDisposed(DisposeEvent e) {
                System.out.println("It's over");
            }
        });
        
        text = new StyledText(shell, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
        text.setLayoutData(new GridData(GridData.FILL_BOTH));
        text.setEnabled(false);
        text.setBounds(10, 10, 400, 500);
        text.setVisible(true);

        list = new org.eclipse.swt.widgets.List(shell, SWT.BORDER);
		list.setBounds(420, 10, 100, 500);
		
        text1 = new Text(shell, SWT.BORDER);
        text1.setBounds(10, 520, 400, 20);
        text1.setVisible(true);
        text1.addTraverseListener(new TraverseListener() {
			
			@Override
			public void keyTraversed(TraverseEvent e) {
				 try {
							PrintWriter out = new PrintWriter(server.getOutputStream());
							out.println(text1.getText());
							text.append("Me >> "+ text1.getText() + "\n");
							text1.setText("");
							out.flush();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
			}
		});                    
      }

}