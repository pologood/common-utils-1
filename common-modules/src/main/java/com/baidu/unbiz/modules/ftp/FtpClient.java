package com.baidu.unbiz.modules.ftp;

import java.io.IOException;
import java.util.Date;

import com.baidu.unbiz.modules.ftp.enums.FtpTransferType;
import com.baidu.unbiz.modules.ftp.exception.FtpException;

/**
 * FtpClient
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年5月31日 下午5:16:55
 */
public interface FtpClient {

    String getRemoteHost();

    void setRemoteHost(String remoteHost) throws IOException, FtpException;

    int getRemotePort();

    void setRemotePort(int remotePort) throws FtpException;

    int getTimeout();

    void setTimeout(int millis) throws IOException, FtpException;

    int getNetworkBufferSize();

    void setNetworkBufferSize(int networkBufferSize);

    void setEncoding(String encoding) throws FtpException;

    void setUsername(String username);

    void setPassword(String password);

    void connect() throws IOException, FtpException;

    boolean connected();

    long size(String remoteFile) throws IOException, FtpException;

    String executeCommand(String command) throws FtpException, IOException;

    String system() throws FtpException, IOException;

    FtpTransferType getType();

    void setType(FtpTransferType type) throws IOException, FtpException;

    void resume() throws FtpException;

    void cancelResume() throws IOException, FtpException;

    void cancelTransfer();

    String pwd() throws IOException, FtpException;

    String put(String localPath, String remoteFile) throws IOException, FtpException;

    String put(String localPath, String remoteFile, boolean append) throws IOException, FtpException;

    Date modtime(String remoteFile) throws IOException, FtpException;

    void setModTime(String remoteFile, Date modTime) throws IOException, FtpException;

    void quit() throws IOException, FtpException;

    void quitImmediately() throws IOException, FtpException;

    void disconnect() throws FtpException, IOException;

    void disconnect(boolean immediate) throws FtpException, IOException;

    void chdir(String dir) throws IOException, FtpException;

    void mkdir(String dir) throws IOException, FtpException;

}