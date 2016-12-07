
package com.baidu.unbiz.modules.ftp.socket;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * FtpSocket
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年5月31日 下午7:20:01
 */
public interface FtpSocket extends Closeable {

    boolean isConnected();

    InputStream getInputStream() throws IOException;

    void setSoTimeout(int timeout) throws SocketException;

    int getSoTimeout() throws SocketException;

    OutputStream getOutputStream() throws IOException;

    InetAddress getLocalAddress();

    int getLocalPort();

    InetAddress getInetAddress();

    String getRemoteHost();

    void setRemoteHost(String remoteHost);

    int getReceiveBufferSize() throws SocketException;

    void setReceiveBufferSize(int size) throws SocketException;

    void setSendBufferSize(int size) throws SocketException;

    int getSendBufferSize() throws SocketException;

    boolean isSecureMode();

    String getDetail();
}
