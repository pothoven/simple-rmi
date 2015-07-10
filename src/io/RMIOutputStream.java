package io;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * from https://www.censhare.com/en/insight/overview/article/file-streaming-using-java-rmi
 */

/**
 * @author pothoven
 *
 */
public class RMIOutputStream extends OutputStream implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private RMIOutputStreamInterf out;
    
    public RMIOutputStream(RMIOutputStreamImpl out) {
        this.out = out;
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
