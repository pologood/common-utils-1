package com.baidu.unbiz.modules.ftp.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import com.baidu.unbiz.common.logger.LoggerSupport;

/**
 * FtpActiveSocket
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年5月31日 下午7:31:23
 */
public class FtpActiveSocket extends LoggerSupport implements FtpDataSocket {

    protected ServerSocket socket;

    protected Socket acceptedSocket;

    protected int sendBufferSize;

    private InetAddress localAddress;

    public FtpActiveSocket(ServerSocket socket) {
        this.socket = socket;
    }

    public void setTimeout(int millis) throws IOException {
        socket.setSoTimeout(millis);
        if (acceptedSocket != null) {
            acceptedSocket.setSoTimeout(millis);
        }

    }

    public void setReceiveBufferSize(int size) throws IOException {
        socket.setReceiveBufferSize(size);
        if (acceptedSocket != null) {
            acceptedSocket.setReceiveBufferSize(size);
        }
    }

    public void setSendBufferSize(int size) throws IOException {
        this.sendBufferSize = size;
        if (acceptedSocket != null) {
            acceptedSocket.setSendBufferSize(size);
        }
    }

    public int getLocalPort() {
        return socket.getLocalPort();
    }

    public InetAddress getLocalAddress() {
        if (localAddress != null) {
            return localAddress;
        }

        return socket.getInetAddress();
    }

    public void setLocalAddress(InetAddress addr) {
        localAddress = addr;
    }

    protected void acceptConnection() throws IOException {
        logger.debug("Calling accept()");
        acceptedSocket = socket.accept();
        acceptedSocket.setSoTimeout(socket.getSoTimeout());
        acceptedSocket.setReceiveBufferSize(socket.getReceiveBufferSize());
        if (sendBufferSize > 0) {
            acceptedSocket.setSendBufferSize(sendBufferSize);
        }

        logger.debug("accept() succeeded");
    }

    public OutputStream getOutputStream() throws IOException {
        acceptConnection();
        return acceptedSocket.getOutputStream();
    }

    public InputStream getInputStream() throws IOException {
        acceptConnection();
        return acceptedSocket.getInputStream();
    }

    public void close() throws IOException {
        closeChild();
        socket.close();
        logger.debug("close() succeeded");
    }

    public void closeChild() throws IOException {
        if (acceptedSocket != null) {
            acceptedSocket.close();
            acceptedSocket = null;
            logger.debug("closeChild() succeeded");
        }
    }
}
