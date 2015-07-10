import io.RMIInputStream;
import io.RMIInputStreamImpl;
import io.RMIOutputStream;
import io.RMIOutputStreamImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

/**
 * 
 */

/**
 * @author pothoven
 *
 */
public class SimpleImpl extends UnicastRemoteObject implements Simple {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final int DEFAULT_MAX_THREAD_COUNT = 5;
	
    private static Vector<Thread> pendingCommandThreads = new Vector<Thread>();
    private static Vector<Thread> runningCommandThreads = new Vector<Thread>();



	public SimpleImpl() throws RemoteException {}

    /* (non-Javadoc)
	 * @see Simple#ping()
	 */
	@Override
    public String ping() { return "Hello world!"; }
    

	/* (non-Javadoc)
	 * @see Simple#runCommand(java.lang.String, java.lang.String[])
	 */
	@Override
	public String runCommand(String command, String[] envp)
			throws RemoteException {

		CommandThread t = new CommandThread(command, envp);
		try {

			if (getActiveThreadCount() < getMaxThreadCount()) {
				runningCommandThreads.add(t);
				t.start();
			} else {
				pendingCommandThreads.add(t);
				System.out.println("Queued (thread: " + t.getName() + "): " + command);
			}
			t.join();

		} catch (Exception e) {
			throw new RemoteException(e.getMessage());
		}

		return t.getResults();
	}
	
	
	/* (non-Javadoc)
	 * @see Simple#getOutputStream(java.io.File)
	 */
	public OutputStream getOutputStream(File f) throws IOException {
		System.out.println("Upload file: " + f.getName());
	    return new RMIOutputStream(new RMIOutputStreamImpl(new FileOutputStream(f)));
	}

	/* (non-Javadoc)
	 * @see Simple#getInputStream(java.io.File)
	 */
	public InputStream getInputStream(File f) throws IOException {
		System.out.println("Download file: " + f.getName());
	    return new RMIInputStream(new RMIInputStreamImpl(new FileInputStream(f)));
	}
	
	
	
	
    /**
     * Get the number of pending command threads.  
     * 
     * @return number of pending command threads
     */
    public static int getPendingThreadCount() {
        return pendingCommandThreads.size();
    }

    /**
     * Get the number of active command threads.  
     * 
     * @return number of active command threads
     */
    public static int getActiveThreadCount() {
        return runningCommandThreads.size();
    }
    
    protected int  getMaxThreadCount() { return DEFAULT_MAX_THREAD_COUNT; }

	
	class CommandThread extends Thread {
		private String command = null;
		private String[] envp = null;
		private StringBuffer results = new StringBuffer();

		
		public CommandThread(String command, String[] envp) {
			super();
			this.command = command;
			this.envp = envp;
		}

		public void run() {
			long startTime = System.currentTimeMillis();
			
            // give a random time up to 2 sec delay before starting new threads 
            // (if this isn't the first thread) to help prevent traffic jams
            if (SimpleImpl.getActiveThreadCount() > 1)
            {
                try {
                    sleep((long) (Math.random() * 2000));
                } catch (InterruptedException e1) { }
            }
            
			try {
				System.out.println("Running (thread: " + this.getName() + ") : " + command);

				Process cmdProcess = Runtime.getRuntime().exec(command, envp);

				BufferedReader stdInput = new BufferedReader(new
						InputStreamReader(cmdProcess.getInputStream()));

				BufferedReader stdError = new BufferedReader(new
						InputStreamReader(cmdProcess.getErrorStream()));

				String s = null;

				/* read the output from the command */
				while ((s = stdInput.readLine()) != null) {
					results.append(s);
				}

				/* read any errors from the attempted command  */
				while ((s = stdError.readLine()) != null) {
					results.append(s);
				}

			} catch (IOException e) {
				results.append(e.getMessage());
            } finally {
            	long endTime = System.currentTimeMillis();
                runningCommandThreads.remove(this);
                System.out.println("Completed (thread: " + this.getName() + ") in "+(endTime - startTime) + " ms");

                // start up the next pending thread
                if (getPendingThreadCount() > 0 &&
                    getActiveThreadCount() < getMaxThreadCount()) {
                    Thread t = pendingCommandThreads.remove(0);
                    runningCommandThreads.add(t);
                    t.start();
                }
            }
		}

		/**
		 * @return the results
		 */
		public String getResults() {
			return results.toString();
		}

	}
}
