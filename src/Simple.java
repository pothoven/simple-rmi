import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.RemoteException;

/**
 * @author pothoven
 *
 */
/**
 * @author pothoven
 *
 */
public interface Simple extends java.rmi.Remote {
	/**
	 * check if server is available
	 * 
	 * @return
	 * @throws RemoteException
	 */
	String ping() throws RemoteException;
	
	/**
	 * run a command on the report server
	 * 
	 * @param command
	 * @param envp
	 * @return
	 * @throws RemoteException
	 */
	String runCommand(String command, String[] envp) throws RemoteException;
	
	/**
	 * Get an output stream for a file to allow file downloads
	 * 
	 * @param File f
	 * @return
	 * @throws IOException
	 */
	OutputStream getOutputStream(File f) throws IOException;
	
	/**
	 * Get an input stream for a file to allow file uploads
	 * 
	 * @param File f
	 * @return
	 * @throws IOException
	 */
	InputStream getInputStream(File f) throws IOException;
}