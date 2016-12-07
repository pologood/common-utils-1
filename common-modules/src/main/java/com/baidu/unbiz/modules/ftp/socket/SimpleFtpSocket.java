package com.baidu.unbiz.modules.ftp.socket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * SimpleFtpSocket
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年5月31日 下午7:22:16
 */
public class SimpleFtpSocket extends Socket implements FtpSocket {

    private String remoteHost;

    public SimpleFtpSocket() {

    }

    public SimpleFtpSocket(String host, int port) throws IOException {
        super(host, port);
    }

    public SimpleFtpSocket(InetAddress addr, int port) throws IOException {
        super(addr, port);
    }

    @Override
    public boolean isSecureMode() {
        return false;
    }

    @Override
    public String getRemoteHost() {
        return remoteHost;
    }

    @Override
    public void setRemoteHost(String remoteHost) {
        this.remoteHost = remoteHost;
    }

    @Override
    public String getDetail() {
        return toString();
    }

    public static SimpleFtpSocket createPlainSocket(String host, int port, int timeout) throws IOException {
        SimpleFtpSocket sock = new SimpleFtpSocket();
        InetSocketAddress addr = new InetSocketAddress(host, port);
        sock.connect(addr, timeout);
        return sock;
    }

    public static SimpleFtpSocket createPlainSocket(InetAddress host, int port, int timeout) throws IOException {
        SimpleFtpSocket sock = new SimpleFtpSocket();
        InetSocketAddress addr = new InetSocketAddress(host, port);
        sock.connect(addr, timeout);
        return sock;
    }
}
