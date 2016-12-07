
package com.baidu.unbiz.modules.ftp.socket;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;

/**
 * FTPDataSocket
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年5月31日 下午7:30:08
 */
public interface FtpDataSocket extends Closeable {

    void setTimeout(int millis) throws IOException;

    void setReceiveBufferSize(int size) throws IOException;

    void setSendBufferSize(int size) throws IOException;

    int getLocalPort();

    InetAddress getLocalAddress();

    OutputStream getOutputStream() throws IOException;

    InputStream getInputStream() throws IOException;

    void closeChild() throws IOException;
}
