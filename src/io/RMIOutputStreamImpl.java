package io;

import java.io.IOException;
import java.io.OutputStream;
import java.rmi.server.UnicastRemoteObject;

/**
 * from https://www.censhare.com/en/insight/overview/article/file-streaming-using-java-rmi
 */


/**
 * @author pothoven
 *
 */
public class RMIOutputStreamImpl implements RMIOutputStreamInterf {
    private OutputStream out;
    
    public RMIOutputStreamImpl(OutputStream out) throws IOException {
        this.out = out;
        UnicastRemoteObject.exportObject(this, 1099);
    }
    
    public void write(int b) throws IOException {
        out.write(b);
    }

    public void write(byte[] b, int off, int len) throws 
            IOException {
        out.write(b, off, len);
    }

    public void close() throws IOException {
        out.close();
    }
}
