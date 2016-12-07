package com.baidu.unbiz.modules.ftp.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

/**
 * FtpPassiveSocket
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年5月31日 下午7:32:14
 */
public class FtpPassiveSocket implements FtpDataSocket {

    protected FtpSocket socket;

    public FtpPassiveSocket(FtpSocket socket) {
        this.socket = socket;
    }

    public void setTimeout(int millis) throws IOException {
        socket.setSoTimeout(millis);
    }

    public void setReceiveBufferSize(int size) throws IOException {
        socket.setReceiveBufferSize(size);
    }

    public void setSendBufferSize(int size) throws IOException {
        socket.setSendBufferSize(size);
    }

    public int getLocalPort() {
        return socket.getLocalPort();
    }

    public InetAddress getLocalAddress() {
        return socket.getLocalAddress();
    }

    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }

    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    public void close() throws IOException {
        socket.close();
    }

    public void closeChild() throws IOException {
        // does nothing
    }

}
