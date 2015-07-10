import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.rmi.Naming;
import java.rmi.registry.Registry;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class SimpleClient
{
    final public static int BUF_SIZE = 1024 * 64;
    
    /**
     * Copy the input stream to the output stream
     * 
     * @param in
     * @param out
     * @throws IOException
     */
    public static void copy(InputStream in, OutputStream out) 
            throws IOException {
    	
    	byte[] b = new byte[BUF_SIZE];
        int len;
        while ((len = in.read(b)) >= 0) {
        	out.write(b, 0, len);
        }
        in.close();
        out.close();
    }
    
    public static void upload(Simple server, File src, File dest) throws IOException {
        copy (new FileInputStream(src), server.getOutputStream(dest));
    }

    public static void download(Simple server, File src, File dest) throws IOException {
        copy (server.getInputStream(src), new FileOutputStream(dest));
    }
	
    public static void main(String arg[])
    {
    	ResourceBundle properties = PropertyResourceBundle.getBundle("Simple");
		int port = Registry.REGISTRY_PORT;
		try {
			port = Integer.parseInt(properties.getString("server.port"));
		} catch (Exception e) {
			port = Registry.REGISTRY_PORT;
		}
    	String command = null;
    	if (arg.length > 0) {
    		command = arg[0];
    	}
    	
    	if (command != null && command.length() > 0) {

    		boolean useSecurityManager = false;
    		try {
    			Boolean.valueOf(properties.getString("useSecurityManager"));
    		} catch (Exception e) {
    			// default to false
    		}
        	if (useSecurityManager && System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
        	
    		try
    		{
    			String serverIP = System.getProperty("server.ip");
    			if (null == serverIP) {
    				try {
    					serverIP = properties.getString("server.ip");
    				} catch (MissingResourceException e) {
    					throw new Exception("Undefined server IP.  Please define 'server.ip' as system property (ex. java -Dserver.ip=xxx) or in the Simple.properties file.");
    				}
    			}
    					
    			Simple server = (Simple) Naming.lookup( "//" +
    					serverIP +
    					":" + port +
    					"/SimpleServer");

    			if ( command.equalsIgnoreCase("ping") ) {
    				System.out.println(server.ping());

    			} else if (command.equalsIgnoreCase("upload") ) {
    				if (arg.length > 1) {
    					String srcFilename = arg[1];
    					if (srcFilename != null && srcFilename.length() > 0) {
    						String destFilename = srcFilename;
    	    				if (arg.length > 2) {
    	    					destFilename = arg[2];
    	    				} 
    						upload(server, new File(srcFilename), new File(destFilename));
    					}
    				}

    			} else if (command.equalsIgnoreCase("download") ) {
    				if (arg.length > 1) {
    					String srcFilename = arg[1];
    					if (srcFilename != null && srcFilename.length() > 0) {
    						String destFilename = srcFilename;
    	    				if (arg.length > 2) {
    	    					destFilename = arg[2];
    	    				} 
    	    				download(server, new File(srcFilename), new File(destFilename));
    					}
    				}

    			} else {
    				System.out.println(server.runCommand(command, null));
    			}
    		}
    		catch (Exception e)
    		{
    			System.out.println("SimpleClient exception: " + e.getMessage());
    			e.printStackTrace();
    		}
    	} else {
    		System.out.println("Usage: SimpleClient command");
    		System.out.println("\nExample: java [-Djava.security.policy=rmi.policy] -jar simple-client.jar ping");
    		System.out.println("\n         java [-Djava.security.policy=rmi.policy] -jar simple-client.jar upload srcfile.txt [destfile.txt]");
    		System.out.println("\n         java [-Djava.security.policy=rmi.policy] -jar simple-client.jar download afile.txt [destfile.txt]");
    		System.out.println("\n         java [-Djava.security.policy=rmi.policy] -jar simple-client.jar \"db2 reorg indexes all for table ADWSRNCT.F_INCIDENT\"");
    	}
    }
} 