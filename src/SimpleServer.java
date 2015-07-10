import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * @author pothoven
 *
 */
public class SimpleServer 
{

	public static void main(String args[])
    {
		ResourceBundle properties = PropertyResourceBundle.getBundle("Simple");
		int port = Registry.REGISTRY_PORT;
		try {
			port = Integer.parseInt(properties.getString("server.port"));
		} catch (Exception e) {
			// default to Registry.REGISTRY_PORT
		}
		
        try
        {
    		boolean useSecurityManager = false;
    		try {
    			Boolean.valueOf(properties.getString("useSecurityManager"));
    		} catch (Exception e) {
    			// default to false
    		}

        	if (useSecurityManager && System.getSecurityManager() == null) {
                System.setSecurityManager(new SecurityManager());
            }
        	
        	Registry registry = LocateRegistry.createRegistry(port);
        	
            SimpleImpl obj = new SimpleImpl();
            /* Bind this object instance to the name "SimpleServer" */
            // Naming.rebind("SimpleServer", obj);
            registry.rebind("SimpleServer", obj);
            
            System.out.println("SimpleServer started on port " + port);
        }
        catch (Exception e)
        {
            System.out.println("SimpleServer err: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 
