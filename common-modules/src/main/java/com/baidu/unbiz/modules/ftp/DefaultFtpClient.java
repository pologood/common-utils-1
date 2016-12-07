/**
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.ftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.baidu.unbiz.common.DateUtil;
import com.baidu.unbiz.common.StringPool;
import com.baidu.unbiz.common.StringUtil;
import com.baidu.unbiz.common.io.StreamUtil;
import com.baidu.unbiz.common.logger.LoggerSupport;
import com.baidu.unbiz.modules.ftp.enums.FtpConnectMode;
import com.baidu.unbiz.modules.ftp.enums.FtpTransferType;
import com.baidu.unbiz.modules.ftp.exception.ControlChannelIOException;
import com.baidu.unbiz.modules.ftp.exception.FtpException;
import com.baidu.unbiz.modules.ftp.exception.FtpTransferCancelledException;
import com.baidu.unbiz.modules.ftp.exception.InvalidReplyException;
import com.baidu.unbiz.modules.ftp.socket.FtpDataSocket;

/**
 * DefaultFtpClient
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年5月31日 下午5:22:49
 */
public class DefaultFtpClient extends LoggerSupport implements FtpClient, FtpClientConstant, StringPool.Charset,
        StringPool.Symbol {

    private InetAddress remoteAddr;

    private String remoteHost;

    private int remotePort = DEFAULT_PORT;

    private volatile boolean resume;

    private long resumeMarker;

    private int timeout = DEFAULT_TIMEOUT;

    private int transferBufferSize = DEFAULT_BUFFER_SIZE;

    private int dataReceiveBufferSize = DEFAULT_TCP_BUFFER_SIZE;

    private int dataSendBufferSize = DEFAULT_TCP_BUFFER_SIZE;

    private int retryCount = DEFAULT_RETRY_COUNT;

    private int retryDelay = DEFAULT_RETRY_DELAY;

    private String username;

    private String password;

    private String encoding = UTF_8;

    private SimpleDateFormat tsFormat = new SimpleDateFormat(DateUtil.FULL_CHINESE_PATTERN);

    private FtpResponse lastReply;

    private FtpResponse lastValidReply;

    private FtpProtocol ftpProtocol;

    private int lowPort = -1;

    private int highPort = -1;

    private String activeIP;

    // FIXME
    private FtpTransferType transferType = FtpTransferType.BINARY;

    // FIXME
    private FtpConnectMode connectMode = FtpConnectMode.ACTIVE;

    private volatile boolean cancelTransfer;

    protected BandwidthThrottler throttler;

    protected FtpDataSocket data;

    private String storeCommand = STORE_CMD;

    @Override
    public String getRemoteHost() {
        return remoteHost;
    }

    @Override
    public void setRemoteHost(String remoteHost) throws IOException, FtpException {
        checkConnection(false);
        this.remoteHost = remoteHost;
        remoteAddr = InetAddress.getByName(remoteHost);
    }

    @Override
    public int getRemotePort() {
        return remotePort;
    }

    @Override
    public void setRemotePort(int remotePort) throws FtpException {
        checkConnection(false);
        this.remotePort = remotePort;
    }

    @Override
    public int getTimeout() {
        return timeout;
    }

    @Override
    public void setTimeout(int millis) throws IOException, FtpException {
        this.timeout = millis;
        if (ftpProtocol != null) {
            ftpProtocol.setTimeout(millis);
        }
    }

    @Override
    public int getNetworkBufferSize() {
        return dataReceiveBufferSize;
    }

    @Override
    public void setNetworkBufferSize(int networkBufferSize) {
        dataReceiveBufferSize = dataSendBufferSize = networkBufferSize;
    }

    @Override
    public void setEncoding(String encoding) throws FtpException {
        checkConnection(false);
        this.encoding = encoding;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void connect() throws IOException, FtpException {
        checkConnection(false);
        logger.debug("Connecting to {}:{}", remoteAddr, remotePort);
        initialize(new FtpProtocol(remoteAddr, remotePort, timeout, encoding));
        login(username, password);
    }

    @Override
    public boolean connected() {
        if (ftpProtocol == null) {
            return false;
        }

        return ftpProtocol.controlSock == null ? false : ftpProtocol.controlSock.isConnected();
    }

    @Override
    public long size(String remoteFile) throws IOException, FtpException {
        checkConnection(true);
        lastReply = ftpProtocol.sendCommand("SIZE " + remoteFile);
        lastValidReply = ftpProtocol.validateReply(lastReply, "213");
        // parse the reply string .
        String replyText = lastValidReply.getReplyText();
        // trim off any trailing characters after a space, e.g. webstar
        // responds to SIZE with 213 55564 bytes
        int spacePos = replyText.indexOf(' ');
        if (spacePos >= 0) {
            replyText = replyText.substring(0, spacePos);
        }

        // parse the reply
        try {
            return Long.parseLong(replyText);
        } catch (NumberFormatException ex) {
            throw new FtpException("Failed to parse reply: {}", replyText);
        }
    }

    @Override
    public String executeCommand(String command) throws FtpException, IOException {
        checkConnection(true);

        lastValidReply = ftpProtocol.sendCommand(command);
        return lastValidReply.getRawReply();
    }

    @Override
    public String system() throws FtpException, IOException {
        checkConnection(true);

        lastReply = ftpProtocol.sendCommand("SYST");
        String[] validCodes = { "200", "213", "215", "250" }; // added 250 for leitch
        lastValidReply = ftpProtocol.validateReply(lastReply, validCodes);
        return lastValidReply.getReplyText();
    }

    @Override
    public FtpTransferType getType() {
        return transferType;
    }

    @Override
    public void setType(FtpTransferType type) throws IOException, FtpException {
        checkConnection(true);
        // determine the character to send
        String typeStr = type.value();
        // send the command
        String[] validCodes = { "200", "250" };
        lastReply = ftpProtocol.sendCommand("TYPE " + typeStr);
        lastValidReply = ftpProtocol.validateReply(lastReply, validCodes);
        // record the type
        transferType = type;
    }

    @Override
    public void resume() throws FtpException {
        if (transferType.equals(FtpTransferType.ASCII)) {
            throw new FtpException("Resume only supported for BINARY transfers");
        }

        resume = true;
        logger.info("Resume=true");
    }

    @Override
    public void cancelResume() throws IOException, FtpException {
        try {
            restart(0);
        } catch (FtpException ex) {
            logger.error("REST failed which is ok (" + ex.getMessage() + ")");
        }

        resumeMarker = 0;
        resume = false;
    }

    public void restart(long size) throws IOException, FtpException {
        lastReply = ftpProtocol.sendCommand("REST " + size);
        lastValidReply = ftpProtocol.validateReply(lastReply, "350");
    }

    @Override
    public void cancelTransfer() {
        cancelTransfer = true;
        logger.warn("cancelTransfer() called");
    }

    @Override
    public String pwd() throws IOException, FtpException {
        checkConnection(true);
        lastReply = ftpProtocol.sendCommand("PWD");
        lastValidReply = ftpProtocol.validateReply(lastReply, "257");
        // get the reply text and extract the dir
        // listed in quotes, if we can find it. Otherwise
        // just return the whole reply string
        String text = lastValidReply.getReplyText();
        int start = text.indexOf('"');
        int end = text.lastIndexOf('"');
        if (start >= 0 && end > start) {
            return text.substring(start + 1, end);
        }

        return text;
    }

    public void login(String username, String password) throws IOException, FtpException {
        checkConnection(true);
        this.username = username;
        this.password = password;
        username(username);
        if (lastValidReply.getReplyCode().equals("230") || lastValidReply.getReplyCode().equals("232")) {
            return;
        }

        password(password);
    }

    public void username(String username) throws IOException, FtpException {
        checkConnection(true);
        this.username = username;
        lastReply = ftpProtocol.sendCommand("USER " + username);
        // we allow for a site with no password - 230 response
        String[] validCodes = { "230", "232", "331" };
        lastValidReply = ftpProtocol.validateReply(lastReply, validCodes);
    }

    public void password(String password) throws IOException, FtpException {
        checkConnection(true);
        this.password = password;
        lastReply = ftpProtocol.sendCommand("PASS " + password);
        // we allow for a site with no passwords (202) or requiring
        // ACCT info (332)
        String[] validCodes = { "230", "202", "332" };
        lastValidReply = ftpProtocol.validateReply(lastReply, validCodes);
    }

    @Override
    public String put(String localPath, String remoteFile) throws IOException, FtpException {
        return put(localPath, remoteFile, false);
    }

    @Override
    public String put(String localPath, String remoteFile, boolean append) throws IOException, FtpException {
        String cwd = safePwd();
        FtpTransferType previousType = transferType;

        try {
            InputStream srcStream = null;
            if (retryCount == 0 || append) {
                srcStream = new FileInputStream(localPath);
                return putStream(srcStream, remoteFile, append);
            }

            return tryPut(cwd, localPath, remoteFile, append);

        } finally {
            resetTransferMode(previousType);
        }

    }

    private String tryPut(String cwd, String localPath, String remoteFile, boolean append) throws FtpException,
            ControlChannelIOException, IOException {
        for (int attempt = 1;; attempt++) {
            try {
                if (attempt > 1 && getType().equals(FtpTransferType.BINARY)) {
                    resume();
                }

                logger.debug("Attempt #{}", attempt);
                InputStream srcStream = new FileInputStream(localPath);
                remoteFile = putStream(srcStream, remoteFile, append);
                break;
            } catch (ControlChannelIOException e) {
                if (!processControlChannelException(cwd, e, attempt)) {
                    throw e;
                }

            } catch (InvalidReplyException e) {
                throw e;
            } catch (IOException e) {
                if (!processTransferException(e, attempt)) {
                    throw e;
                }
            }
        }

        return remoteFile;
    }

    @Override
    public Date modtime(String remoteFile) throws IOException, FtpException {
        checkConnection(true);
        lastReply = ftpProtocol.sendCommand("MDTM " + remoteFile);
        lastValidReply = ftpProtocol.validateReply(lastReply, "213");
        // parse the reply string ...
        Date ts = tsFormat.parse(lastValidReply.getReplyText(), new ParsePosition(0));
        return ts;
    }

    @Override
    public void setModTime(String remoteFile, Date modTime) throws IOException, FtpException {
        checkConnection(true);
        String time = tsFormat.format(modTime);
        lastReply = ftpProtocol.sendCommand("MFMT " + time + " " + remoteFile);
        lastValidReply = ftpProtocol.validateReply(lastReply, "213");
    }

    @Override
    public void quit() throws IOException, FtpException {
        checkConnection(true);
        try {
            lastReply = ftpProtocol.sendCommand("QUIT");
            String[] validCodes = { "221", "226" };
            lastValidReply = ftpProtocol.validateReply(lastReply, validCodes);
        } finally { // ensure we clean up the connection
            try {
                ftpProtocol.logout();
            } finally {
                ftpProtocol = null;
            }
        }

        StreamUtil.close(data);
    }

    @Override
    public void quitImmediately() throws IOException, FtpException {
        cancelTransfer();
        try {
            if (ftpProtocol != null && ftpProtocol.controlSock != null) {
                ftpProtocol.controlSock.close();
            }
        } finally {
            ftpProtocol = null;
        }

        StreamUtil.close(data);
    }

    @Override
    public void disconnect() throws FtpException, IOException {
        quit();
    }

    @Override
    public void disconnect(boolean immediate) throws FtpException, IOException {
        if (immediate) {
            quitImmediately();
            return;
        }

        quit();
    }

    @Override
    public void chdir(String dir) throws IOException, FtpException {
        checkConnection(true);
        lastReply = ftpProtocol.sendCommand("CWD " + dir);
        lastValidReply = ftpProtocol.validateReply(lastReply, "250");
    }

    @Override
    public void mkdir(String dir) throws IOException, FtpException {
        if (existDir(dir)) {
            return;
        }

        // FIXME "/" a/b/c a,b,c
        String[] subDirs = StringUtil.split(dir, SLASH + SPACE + BACK_SLASH);
        String workDir = null;
        for (int i = 0, size = subDirs.length; i < size; i++) {
            if (workDir == null) {
                workDir = subDirs[i];
            } else {
                workDir = workDir + SLASH + subDirs[i];
            }

            doMkdir(workDir);
        }
    }

    private void doMkdir(String dir) throws IOException, FtpException {
        if (existDir(dir)) {
            return;
        }

        lastReply = ftpProtocol.sendCommand("MKD " + dir);
        // some servers return 200,257, technically incorrect but we cater for it ...
        String[] validCodes = { "200", "250", "257" };
        lastValidReply = ftpProtocol.validateReply(lastReply, validCodes);
    }

    private boolean existDir(String remoteDir) throws FtpException, IOException {
        String initDir = pwd();
        try {
            chdir(remoteDir);
        } catch (Exception e) {
            return false;
        }

        chdir(initDir);
        return true;
    }

    private void validateTransfer() throws IOException, FtpException {
        checkConnection(true);
        // check the control response
        String[] validCodes = { "225", "226", "250" };
        lastReply = ftpProtocol.readReply();
        // if we cancelled the transfer throw an exception
        if (cancelTransfer) {
            lastValidReply = lastReply;
            logger.warn("Transfer has been cancelled!");
            throw new FtpTransferCancelledException();
        }

        lastValidReply = ftpProtocol.validateReply(lastReply, validCodes);
    }

    public void setThrottler(BandwidthThrottler throttler) {
        this.throttler = throttler;
    }

    private void checkConnection(boolean shouldBeConnected) throws FtpException {
        if (shouldBeConnected && !connected()) {
            throw new FtpException("The FTP client has not yet connected to the server.  "
                    + "The requested action cannot be performed until after a connection has been established.");
        }

        if (!shouldBeConnected && connected()) {
            throw new FtpException("The FTP client has already been connected to the server.  "
                    + "The requested action must be performed before a connection is established.");
        }

    }

    private void initialize(FtpProtocol control) throws IOException {
        this.ftpProtocol = control;
        control.setTimeout(timeout);
        if (activeIP != null) {
            control.setActivePortIPAddress(activeIP);
        }

        if (lowPort > 0 && highPort > 0) {
            control.setActivePortRange(lowPort, highPort);
        }
    }

    private String safePwd() throws IOException {
        try {
            return pwd();
        } catch (FtpException e) {
            logger.error("Ignoring exception: {}", e.getMessage());
            return null;
        }
    }

    private String putStream(InputStream srcStream, String remoteFile, boolean append) 
            throws IOException, FtpException {
        try {
            remoteFile = putData(srcStream, remoteFile, append);
            validateTransfer();
            return remoteFile;
        } catch (FtpException e) {
            throw e;
        } catch (ControlChannelIOException e) {
            throw e;
        } catch (IOException e) {
            validateTransferOnError(e);
            throw e;
        }
    }

    private InputStream prepareInputStream(InputStream srcStream) throws IOException {
        InputStream in = new BufferedInputStream(srcStream);
        // if resuming, we skip over the unwanted bytes
        if (resume && resumeMarker > 0) {
            in.skip(resumeMarker);
        } else {
            resumeMarker = 0;
        }

        return in;
    }

    static class MatchposAndSize {
        long size;
        int matchpos;

        MatchposAndSize(long size, int matchpos) {
            this.size = size;
            this.matchpos = matchpos;
        }
    }

    private MatchposAndSize putAscii(OutputStream out, int count, byte[] buf) throws IOException {
        long size = 0;
        byte[] prevBuf = new byte[FTP_LINE_SEPARATOR.length];
        int matchpos = 0;
        // we want to allow \r\n, \r and \n
        for (int i = 0; i < count; i++) {
            // LF without preceding CR (i.e. Unix text file)
            if (buf[i] == LINE_FEED && matchpos == 0) {
                out.write(CARRIAGE_RETURN);
                out.write(LINE_FEED);
                size += 2;
                continue;
            }
            if (buf[i] == FTP_LINE_SEPARATOR[matchpos]) {
                prevBuf[matchpos] = buf[i];
                matchpos++;
                if (matchpos == FTP_LINE_SEPARATOR.length) {
                    out.write(CARRIAGE_RETURN);
                    out.write(LINE_FEED);
                    size += 2;
                    matchpos = 0;
                }
                continue;
            }
            // no match current char
            // this must be a matching \r if we matched first char
            if (matchpos > 0) {
                out.write(CARRIAGE_RETURN);
                out.write(LINE_FEED);
                size += 2;
            }
            out.write(buf[i]);
            size++;
            matchpos = 0;

        }

        return new MatchposAndSize(size, matchpos);
    }

    private String putData(InputStream srcStream, String remoteFile, boolean append) throws IOException, FtpException {
        IOException storedEx = null;
        InputStream in = null;
        OutputStream out = null;
        long size = 0;
        try {
            in = prepareInputStream(srcStream);
            remoteFile = initPut(remoteFile, append);
            // get an output stream
            out = new BufferedOutputStream(new DataOutputStream(data.getOutputStream()), transferBufferSize * 2);
            byte[] buf = new byte[transferBufferSize];
            int matchpos = 0;

            boolean isASCII = getType() == FtpTransferType.ASCII;
            if (throttler != null) {
                throttler.reset();
            }

            for (int count = 0; (count = in.read(buf)) > 0 && !cancelTransfer;) {
                if (isASCII) {
                    MatchposAndSize mas = putAscii(out, count, buf);
                    matchpos = mas.matchpos;
                    size = mas.size;
                } else { // binary
                    out.write(buf, 0, count);
                    size += count;
                }

                if (throttler != null) {
                    throttler.throttleTransfer(size);
                }

            }
            // write out anything left at the end that has been saved
            // - must be a \r which we convert into a line terminator
            if (isASCII && matchpos > 0) {
                out.write(CARRIAGE_RETURN);
                out.write(LINE_FEED);
                size += 2;
            }
        } catch (IOException e) {
            storedEx = e;
            logger.error("Caught and rethrowing exception in getDataAfterInitGet()", e);
        } finally {
            resume = false;
            resumeMarker = 0;
            StreamUtil.close(in);
            closeDataSocket(out);
            // if we failed to write the file, rethrow the exception
            if (storedEx != null) {
                throw storedEx;
            }
            // log bytes transferred
            logger.debug("Transferred {} bytes to remote host", size);
        }

        return remoteFile;
    }

    // FIXME
    protected String initPut(String remoteFile, boolean append) throws IOException, FtpException {
        checkConnection(true);
        // if a remote filename isn't supplied, assume STOU is to be used
        boolean storeUnique = (StringUtil.isEmpty(remoteFile));
        if (storeUnique) {
            remoteFile = EMPTY;
            // check STOU isn't used with append
            if (append) {
                String msg = "A remote filename must be supplied when appending";
                logger.error(msg);
                throw new FtpException(msg);
            }
        }
        // reset the cancel flag
        cancelTransfer = false;
        boolean close = false;
        try {
            resumeMarker = 0;
            // if resume is requested, we must obtain the size of the remote file
            if (resume) {
                if (transferType.equals(FtpTransferType.ASCII)) {
                    throw new FtpException("Resume only supported for BINARY transfers");
                }

                try {
                    resumeMarker = size(remoteFile);
                } catch (FtpException e) {
                    resumeMarker = 0;
                    resume = false;
                    logger.warn("SIZE failed '{}' - resume will not be used ({})", remoteFile, e.getMessage());
                }
            }
            // set up data channel
            setupDataSocket();
            // issue REST
            if (resume) {
                try {
                    restart(resumeMarker);
                } catch (FtpException e) {
                    resumeMarker = 0;
                    resume = false;
                    logger.warn("REST failed - resume will not be used ({})", e.getMessage());
                }
            }

            remoteFile = sendStoreCmd(remoteFile, append, storeUnique);
            return remoteFile;
        } catch (IOException e) {
            close = true;
            logger.error("Caught and rethrowing exception in initPut()", e);
            throw e;
        } catch (FtpException e) {
            close = true;
            logger.error("Caught and rethrowing exception in initPut()", e);
            throw e;
        } finally {
            if (close) {
                resume = false;
                resumeMarker = 0;
                StreamUtil.close(data);
            }
        }
    }

    private String sendStoreCmd(String remoteFile, boolean append, boolean storeUnique) throws FtpException,
            IOException {
        // send the command to store
        String cmd = append ? "APPE " : (storeUnique ? "STOU" : storeCommand);
        lastReply = ftpProtocol.sendCommand(cmd + remoteFile);
        // Can get a 125 or a 150, also allow 350 (for Global eXchange Services server)
        // JScape returns 151
        String[] validCodes = { "125", "150", "151", "350" };
        lastValidReply = ftpProtocol.validateReply(lastReply, validCodes);
        String replyText = lastValidReply.getReplyText();
        if (!storeUnique) {
            return remoteFile;
        }

        int pos = replyText.indexOf(STOU_FILENAME_MARKER);
        if (pos >= 0) {
            pos += STOU_FILENAME_MARKER.length();
            return replyText.substring(pos).trim();
        }
        // couldn't find marker, just return last word of reply e.g. 150 Opening BINARY mode data connection for
        // FTP0000004.
        logger.debug("Could not find " + STOU_FILENAME_MARKER + " in reply - using last word instead.");
        pos = replyText.lastIndexOf(' ');
        remoteFile = replyText.substring(++pos);
        int len = remoteFile.length();
        if (len > 0 && remoteFile.charAt(len - 1) == '.') {
            remoteFile = remoteFile.substring(0, len - 1);
        }

        return remoteFile;
    }

    protected void setupDataSocket() throws IOException, FtpException {
        data = ftpProtocol.createDataSocket(connectMode);
        data.setTimeout(timeout);
        if (dataReceiveBufferSize > 0) {
            data.setReceiveBufferSize(dataReceiveBufferSize);
        }

        if (dataSendBufferSize > 0) {
            data.setSendBufferSize(dataSendBufferSize);
        }

    }

    private boolean processControlChannelException(String cwd, Exception ex, int attemptNumber) throws IOException,
            FtpException {
        if (attemptNumber <= retryCount + 1) {
            if (retryDelay > 0) {
                try {
                    logger.debug("Sleeping for {} ms prior to retry", retryDelay);
                    Thread.sleep(retryDelay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            logger.error("Transfer error on attempt #" + attemptNumber + ": reconnecting & retrying: ", ex);
            reconnect(cwd);
            return true;
        }

        logger.info("Failed {} attempts - giving up", attemptNumber);
        return false;

    }

    private boolean processTransferException(Exception ex, int attemptNumber) {
        if (attemptNumber <= retryCount + 1) {
            if (retryDelay > 0) {
                try {
                    logger.debug("Sleeping for {} ms prior to retry", retryDelay);
                    Thread.sleep(retryDelay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            logger.error("Transfer error on attempt #" + attemptNumber + " retrying: ", ex);
            return true;
        }

        if (attemptNumber > 0) {
            logger.info("Failed {} attempts - giving up", attemptNumber);
        }

        return false;

    }

    private void reconnect(String cwd) throws IOException, FtpException {
        try {
            quitImmediately();
        } catch (Exception e) {
            logger.error("quitImmediately error", e);
        }

        logger.info("Reconnecting");
        connect();
        login(username, password);
        setType(transferType);
        if (cwd != null) {
            chdir(cwd); // switch to the target directory
        }
    }

    private void resetTransferMode(FtpTransferType previousType) throws IOException, FtpException {
        if (!transferType.equals(previousType)) {
            setType(previousType);
        }
    }

    private void validateTransferOnError(IOException ex) throws IOException, FtpException {
        logger.debug("Validate transfer on error after exception", ex);
        checkConnection(true);

        ftpProtocol.setTimeout(SHORT_TIMEOUT);
        try {
            validateTransfer();
        } catch (Exception e) {
            logger.warn("Validate transfer on error failed", e);
        } finally {
            ftpProtocol.setTimeout(timeout);
        }
    }

    protected void closeDataSocket(InputStream stream) {
        StreamUtil.close(stream);
        StreamUtil.close(data);
    }

    private void closeDataSocket(OutputStream stream) {
        StreamUtil.close(stream);
        StreamUtil.close(data);
    }

}
