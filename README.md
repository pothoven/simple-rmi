# simple-rmi
Simple Java RMI example to run remote commands and transfer files.  

The server accepts 4 commands:

1. `ping` This will return a simple "Hello World" message just to verify the connection.

2. `upload` This will take 1 or 2 additional arguments.  The first is the local file (client side) to be uploaded. The second (optional) argument is the remote file to be written to.  If no remote file is specified it will use the same file name as the local file.

3. `download` This will take 1 or 2 additional arguments.  The first is the remote file (server side) to be downloaded. The second (optional) argument is the local file to be written to. If no local file is specified it will use the same file name as the remote file.

4. Any valid command-line command to invoke on the remote server.  These commands will be threaded to allow multiple commands to be issued without holding up on a prior command.  The number of concurrent threads is limited to prevent over-loading the server, all subsequent commands are queued up until a thread is available.
