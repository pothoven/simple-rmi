================================================================================
SERVER
================================================================================

* Starting the Server

The server can be started with the command line:

    java -jar simple-server.jar

* Server customization

The server will automatically create an RMI registry so you do not
need to manually start it with the 'rmiregistry' program.  It will use
the default port of 1099 unless an alternative port is specified with
the 'server.port' property in the Simple.properties file.

To keep things simple, this RMI application is not enforcing a Java
security policy by default.  If you wish to utilize a security
plolicy, set the value of 'useSecurityManager' to true in the
Simple.properties file, then specify a policy file such as:

    java -Djava.security.policy=rmi.policy -jar simple-server.jar

================================================================================
CLIENT
================================================================================

* Starting the Client

The client can be started with the command line:

    java -jar simple-client.jar ping

where 'ping' is the command used to check the connection, but any
other valid system command is acceptable.

* Client customization

The hostname or IP address of the server needs to be defined for the
client to connect to.  The server address can either be specified with
a system property or in the Simple.properties file as 'server.ip'.  To
use a system property you can add it to the command line such as:

    java -Dserver.ip=localhost -jar simple-client.jar ping


The server will have started using the default port of 1099 unless an
alternative port is specified with the 'server.port' property in the
Simple.properties file.


To keep things simple, this RMI application is not enforcing a Java
security policy by default.  If you wish to utilize a security
plolicy, set the value of 'useSecurityManager' to true in the
Simple.properties file, then specify a policy file such as:

    java -Djava.security.policy=rmi.policy -jar simple-client.jar ping

