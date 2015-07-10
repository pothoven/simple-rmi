package io;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * from https://www.censhare.com/en/insight/overview/article/file-streaming-using-java-rmi
 */

/**
 * @author pothoven
 *
 */
public interface RMIInputStreamInterf extends Remote {
    
    public byte[] readBytes(int len) throws IOException, RemoteException;
    public int read() throws IOException, RemoteException;
    public void close() throws IOException, RemoteException;

}