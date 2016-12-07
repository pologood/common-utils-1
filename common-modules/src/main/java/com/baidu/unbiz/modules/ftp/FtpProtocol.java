/**
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.List;
import java.util.Random;

import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.common.StringPool;
import com.baidu.unbiz.common.io.StreamUtil;
import com.baidu.unbiz.common.logger.LoggerSupport;
import com.baidu.unbiz.modules.ftp.enums.FtpConnectMode;
import com.baidu.unbiz.modules.ftp.exception.ControlChannelIOException;
import com.baidu.unbiz.modules.ftp.exception.FtpConnectionClosedException;
import com.baidu.unbiz.modules.ftp.exception.FtpException;
import com.baidu.unbiz.modules.ftp.exception.InvalidReplyException;
import com.baidu.unbiz.modules.ftp.socket.SimpleFtpSocket;
import com.baidu.unbiz.modules.ftp.socket.FtpActiveSocket;
import com.baidu.unbiz.modules.ftp.socket.FtpDataSocket;
import com.baidu.unbiz.modules.ftp.socket.FtpPassiveSocket;
import com.baidu.unbiz.modules.ftp.socket.FtpSocket;

/**
 * FtpProtocol
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年5月31日 下午4:55:00
 */
public class FtpProtocol extends LoggerSupport implements StringPool.Charset, StringPool.Symbol, FtpClientConstant {

    private boolean strictReturnCodes = true;

    protected boolean listenOnAllInterfaces = true;

    protected FtpSocket controlSock;

    protected Writer writer;

    protected Reader reader;

    protected String forcedActiveIP;

    private int lowPort = -1;

    private int highPort = -1;

    private int nextPort;

    private String encoding = UTF_8;

    protected InetAddress remoteAddr;

    protected boolean autoPassiveIPSubstitution;

    protected FtpProtocol(InetAddress remoteAddr, int controlPort, int timeout, String encoding)
            throws IOException, FtpException {
        this(remoteAddr, SimpleFtpSocket.createPlainSocket(remoteAddr, controlPort, timeout), timeout, encoding);
    }

    protected FtpProtocol(InetAddress remoteAddr, FtpSocket controlSock, int timeout, String encoding)
            throws IOException, FtpException {
        this.remoteAddr = remoteAddr;
        this.controlSock = controlSock;
        this.encoding = encoding;
        try {
            setTimeout(timeout);
            initStreams();
            validateConnection();
        } catch (IOException e) {
            logger.error("Failed to initialize control socket", e);
            controlSock.close();
            controlSock = null;
            throw e;
        } catch (FtpException e) {
            logger.error("Failed to initialize control socket", e);
            controlSock.close();
            controlSock = null;
            throw e;
        }
    }

    private void validateConnection() throws IOException, FtpException {
        FtpResponse reply = readReply();
        String[] validCodes = { "220", "230" };
        validateReply(reply, validCodes);
    }

    protected void initStreams() throws IOException {
        InputStream is = controlSock.getInputStream();
        reader = new InputStreamReader(is, encoding);

        OutputStream os = controlSock.getOutputStream();
        writer = new OutputStreamWriter(os, encoding);
    }

    public void close() throws IOException {
        controlSock.close();
    }

    public void logout() throws IOException {
        IOException e = StreamUtil.closeAndGetEx(writer);
        e = StreamUtil.closeAndGetEx(writer);
        e = StreamUtil.closeAndGetEx(controlSock);

        if (e != null) {
            throw e;
        }
    }

    FtpDataSocket createDataSocket(FtpConnectMode connectMode) throws IOException, FtpException {
        if (connectMode == FtpConnectMode.ACTIVE) {
            return createDataSocketActive();
        }

        return createDataSocketPassive();
    }

    FtpDataSocket createDataSocketActive() throws IOException, FtpException {
        try {
            int maxCount = MAX_ACTIVE_RETRY;
            if (lowPort >= 0 && highPort >= 0) {
                int range = highPort - lowPort + 1;
                if (range < MAX_ACTIVE_RETRY) {
                    maxCount = range;
                }

            }
            for (int count = 0; count < maxCount;) {
                count++;
                try {
                    FtpDataSocket socket = newActiveDataSocket(nextPort);
                    int port = socket.getLocalPort();
                    InetAddress addr = socket.getLocalAddress();
                    sendPortCommand(addr, port);
                    return socket;
                } catch (SocketException e) {
                    if (count < maxCount) {
                        logger.warn("Detected socket in use - retrying and selecting new port");
                        setNextAvailablePortFromRange();
                    }
                }
            }
            throw new FtpException("Exhausted active port retry count - giving up");
        } finally {
            setNextAvailablePortFromRange();
        }
    }

    private void setNextAvailablePortFromRange() {
        if (lowPort < 0 && highPort < 0) {
            return;
        }

        nextPort = (nextPort == 0) ? lowPort + new Random().nextInt(highPort - lowPort) : nextPort + 1;
        if (nextPort > highPort) {
            nextPort = lowPort;
        }

        logger.debug("Next active port will be: {}", nextPort);
    }

    void sendPortCommand(InetAddress addr, int port) throws IOException, FtpException {
        byte[] hostBytes = addr.getAddress();
        byte[] portBytes = toByteArray(port);

        if (forcedActiveIP != null) {
            logger.info("Forcing use of fixed IP for PORT command");
            hostBytes = getIPAddressBytes(forcedActiveIP);
        }

        // assemble the PORT command
        String cmd = new StringBuilder("PORT ").append(toUnsignedShort(hostBytes[0])).append(",")
                .append(toUnsignedShort(hostBytes[1])).append(",").append(toUnsignedShort(hostBytes[2])).append(",")
                .append(toUnsignedShort(hostBytes[3])).append(",").append(toUnsignedShort(portBytes[0])).append(",")
                .append(toUnsignedShort(portBytes[1])).toString();

        // send command and check reply
        // CoreFTP returns 250 incorrectly
        FtpResponse reply = sendCommand(cmd);
        String[] validCodes = { "200", "250" };
        validateReply(reply, validCodes);
    }

    private short toUnsignedShort(byte value) {
        return (value < 0) ? (short) (value + 256) : (short) value;
    }

    protected byte[] toByteArray(int value) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (value >> 8); // bits 1- 8
        bytes[1] = (byte) (value & 0x00FF); // bits 9-16
        return bytes;
    }

    protected void setAutoPassiveIPSubstitution(boolean autoPassiveIPSubstitution) {
        this.autoPassiveIPSubstitution = autoPassiveIPSubstitution;
    }

    void setActivePortIPAddress(String forcedActiveIP) {
        this.forcedActiveIP = forcedActiveIP;
    }

    public void setActivePortRange(int lowest, int highest) {
        this.lowPort = lowest;
        this.highPort = highest;
        this.nextPort = lowPort;
    }

    private byte[] getIPAddressBytes(String ipAddress) throws FtpException {
        byte[] ipBytes = new byte[4];
        StringBuilder build = new StringBuilder();
        // loop thru and examine each char
        for (int i = 0, partCount = 0, len = ipAddress.length(); i < len && partCount <= 4; i++) {
            char ch = ipAddress.charAt(i);
            if (Character.isDigit(ch)) {
                build.append(ch);
            } else if (ch != '.') {
                throw new FtpException("Incorrectly formatted IP address: " + ipAddress);
            }
            // get the part
            if (ch == '.' || i + 1 == len) { // at end or at separator
                try {
                    ipBytes[partCount++] = (byte) Integer.parseInt(build.toString());
                    build.setLength(0);
                } catch (NumberFormatException ex) {
                    throw new FtpException("Incorrectly formatted IP address: " + ipAddress);
                }
            }
        }

        return ipBytes;
    }

    protected FtpDataSocket createDataSocketPassive() throws IOException, FtpException {
        // PASSIVE command - tells the server to listen for a connection attempt rather than initiating it
        FtpResponse replyObj = sendCommand("PASV");
        validateReply(replyObj, "227");
        String reply = replyObj.getReplyText();
        int[] parts = getPassiveParts(reply);
        // assemble the IP address we try connecting, so we don't bother checking digits etc
        String ipAddress = parts[0] + "." + parts[1] + "." + parts[2] + "." + parts[3];
        // assemble the port number
        int port = (parts[4] << 8) + parts[5];
        String hostIP = ipAddress;
        if (autoPassiveIPSubstitution) {
            if (usingProxy()) { // we can't use the remoteAddr as that will be set to the proxy
                hostIP = controlSock.getRemoteHost();
                logger.debug("Using proxy");
            } else {
                hostIP = remoteAddr.getHostAddress();
            }

            StringBuilder msg = new StringBuilder("Substituting server supplied IP (");
            msg.append(ipAddress).append(") with remote host IP (").append(hostIP).append(")");
            logger.info(msg.toString());
        }

        // create the socket
        return newPassiveDataSocket(hostIP, port);
    }

    protected boolean usingProxy() {
        return false;
    }

    public boolean isSecureMode() {
        return false;
    }

    String getRemoteHostName() {
        InetAddress addr = controlSock.getInetAddress();
        return addr.getHostName();
    }

    void setStrictReturnCodes(boolean strict) {
        this.strictReturnCodes = strict;
    }

    void setListenOnAllInterfaces(boolean listenOnAll) {
        this.listenOnAllInterfaces = listenOnAll;
    }

    boolean getListenOnAllInterfaces() {
        return listenOnAllInterfaces;
    }

    void setTimeout(int millis) throws IOException {
        if (controlSock == null) {
            throw new IllegalStateException("Failed to set timeout - no control socket");
        }

        controlSock.setSoTimeout(millis);
    }

    private String getRelay(String reply, int startIP, int endIP) {
        // if didn't find start bracket, figure out where it should have been
        if (startIP < 0) {
            startIP = 0;
            while (startIP < reply.length() && !Character.isDigit(reply.charAt(startIP))) {
                startIP++;
            }

            startIP--; // go back so this is where the '(' should be
        }

        // if didn't find end bracket, set to end of reply
        if (endIP < 0) {
            endIP = reply.length() - 1;
            while (endIP > 0 && !Character.isDigit(reply.charAt(endIP))) {
                endIP--;
            }

            endIP++; // go forward so this is where the ')' should be
            if (endIP >= reply.length()) {
                reply += ")";
            }
        }

        return reply;
    }

    int[] getPassiveParts(String reply) throws FtpException {
        // The reply to PASV is in the form:
        // 227 Entering Passive Mode (h1,h2,h3,h4,p1,p2). where h1..h4 are the IP address to connect and
        // p1,p2 the port number
        // Example:
        // 227 Entering Passive Mode (128,3,122,1,15,87).
        // NOTE: PASV command in IBM/Mainframe returns the string 227 Entering Passive Mode 128,3,122,1,15,87 (missing
        // brackets) extract the IP data string from between the brackets
        int startIP = reply.indexOf('(');
        int endIP = reply.indexOf(')');
        reply = getRelay(reply, startIP, endIP);

        String ipData = reply.substring(startIP + 1, endIP).trim();
        int[] parts = new int[6];
        StringBuilder builder = new StringBuilder();
        // loop thru and examine each char
        for (int i = 0, partCount = 0, len = ipData.length(); i < len && partCount <= 6; i++) {
            char ch = ipData.charAt(i);
            if (Character.isDigit(ch)) {
                builder.append(ch);
            }

            else if (ch != ',' && ch != ' ') {
                throw new FtpException("Malformed PASV reply: " + reply);
            }
            // get the part
            if (ch == ',' || i + 1 == len) { // at end or at separator
                try {
                    parts[partCount++] = Integer.parseInt(builder.toString());
                    builder.setLength(0);
                } catch (NumberFormatException ex) {
                    throw new FtpException("Malformed PASV reply: " + reply);
                }
            }
        }

        return parts;
    }

    protected FtpDataSocket newPassiveDataSocket(String remoteHost, int port) throws IOException {
        FtpSocket sock = SimpleFtpSocket.createPlainSocket(remoteHost, port, controlSock.getSoTimeout());
        return new FtpPassiveSocket(sock);
    }

    protected FtpDataSocket newActiveDataSocket(int port) throws IOException {
        // ensure server sock gets the timeout
        ServerSocket sock = listenOnAllInterfaces ? new ServerSocket(port) : 
            new ServerSocket(port, 0, controlSock.getLocalAddress());
        logger.debug("ListenOnAllInterfaces={}", listenOnAllInterfaces);
        sock.setSoTimeout(controlSock.getSoTimeout());
        FtpActiveSocket activeSock = new FtpActiveSocket(sock);
        activeSock.setLocalAddress(controlSock.getLocalAddress());
        return activeSock;
    }

    public FtpResponse sendCommand(String command) throws IOException, IOException, FtpException {
        writeCommand(command);
        // and read the result
        return readReply();
    }

    void writeCommand(String command) throws IOException {
        logger(DEBUG_ARROW + command, true);
        // send it
        try {
            writer.write(command + EOL);
            writer.flush();
        } catch (IOException ex) {
            throw new ControlChannelIOException(ex.getMessage());
        }
    }

    private String readLine() throws IOException {
        int current = 0;
        StringBuilder str = new StringBuilder();
        StringBuilder err = new StringBuilder();
        while (true) {
            try {
                current = reader.read();
            } catch (IOException e) {
                logger.error("Read failed ('{}' read so far)", e.toString());
                throw new ControlChannelIOException(e.getMessage());
            }
            if (current < 0) {
                String msg = "Control channel unexpectedly closed ('" + err.toString() + "' read so far)";
                logger.error(msg);
                throw new ControlChannelIOException(msg);
            }
            if (current == LINE_FEED) {
                break;
            }

            if (current != CARRIAGE_RETURN) {
                str.append((char) current);
                err.append((char) current);
            } else {
                err.append("<cr>");
            }
        }

        return str.toString();
    }

    FtpResponse readReply() throws IOException, FtpException {
        String line = readLine();
        while (line != null && line.trim().length() == 0) {
            line = readLine();
        }
        line = line.trim();
        logger(line, false);
        if (line.length() < 3) {
            String msg = "Short reply received (" + line + ")";
            logger.error(msg);
            throw new InvalidReplyException(msg);
        }

        String replyCode = line.substring(0, 3);
        StringBuilder reply = new StringBuilder(EMPTY);
        if (line.length() > 3) {
            reply.append(line.substring(4));
        }

        List<String> dataLines = getDataLines(line, reply, replyCode);

        if (dataLines != null) {
            String[] data = dataLines.toArray(new String[dataLines.size()]);
            return new FtpResponse(replyCode, reply.toString(), data);
        }

        return new FtpResponse(replyCode, reply.toString());
    }

    private List<String> getDataLines(String line, StringBuilder reply, String replyCode) throws IOException {
        // check for multi-line response and build up the reply
        if (!(line.length() > 3 && line.charAt(3) == '-')) {
            return null;
        }

        List<String> dataLines = CollectionUtil.createArrayList();
        // if first line has data, add to data list
        if (line.length() > 4) {
            line = line.substring(4).trim();
            if (line.length() > 0) {
                dataLines.add(line);
            }
        }

        boolean complete = false;
        while (!complete) {
            line = readLine();
            if (line == null) {
                String msg = "Control channel unexpectedly closed";
                logger.error(msg);
                throw new ControlChannelIOException(msg);
            }

            if (line.length() == 0) {
                continue;
            }

            logger(line, false);
            if (line.length() > 3 && line.substring(0, 3).equals(replyCode) && line.charAt(3) == ' ') {
                line = line.substring(3).trim(); // get rid of the code
                if (line.length() > 0) {
                    if (reply.length() > 0) {
                        reply.append(SPACE);
                    }

                    reply.append(line);
                    dataLines.add(line);
                }
                complete = true;
            } else { // not the last line
                reply.append(SPACE).append(line);
                dataLines.add(line);
            }
        }

        return dataLines;
    }

    FtpResponse validateReply(String reply, String expectedReplyCode) throws FtpException {
        FtpResponse replyObj = new FtpResponse(reply);
        if (validateReplyCode(replyObj, expectedReplyCode)) {
            return replyObj;
        }

        // if unexpected reply, throw an exception
        throw new FtpException(replyObj);
    }

    public FtpResponse validateReply(String reply, String[] expectedReplyCodes) throws IOException, FtpException {
        FtpResponse replyObj = new FtpResponse(reply);
        return validateReply(replyObj, expectedReplyCodes);
    }

    public FtpResponse validateReply(FtpResponse reply, String[] expectedReplyCodes) throws FtpException {
        for (int i = 0; i < expectedReplyCodes.length; i++) {
            if (validateReplyCode(reply, expectedReplyCodes[i])) {
                return reply;
            }
        }
        // got this far, not recognised
        StringBuilder build = new StringBuilder("[");
        for (int i = 0; i < expectedReplyCodes.length; i++) {
            build.append(expectedReplyCodes[i]);
            if (i + 1 < expectedReplyCodes.length) {
                build.append(",");
            }
        }

        build.append("]");
        logger.info("Expected reply codes = {}", build);
        throw new FtpException(reply);
    }

    public FtpResponse validateReply(FtpResponse reply, String expectedReplyCode) throws FtpException {
        if (validateReplyCode(reply, expectedReplyCode)) {
            return reply;
        }

        // got this far, not recognised
        logger.info("Expected reply code = [{}]", expectedReplyCode);
        throw new FtpException(reply);
    }

    private boolean validateReplyCode(FtpResponse reply, String expectedReplyCode) throws FtpConnectionClosedException {
        String replyCode = reply.getReplyCode();
        if ("421".equals(replyCode)) {
            throw new FtpConnectionClosedException(reply.getReplyText());
        }
        if (strictReturnCodes) {
            if (replyCode.equals(expectedReplyCode)) {
                return true;
            }

            return false;

        }
        // non-strict - match first char
        if (replyCode.charAt(0) == expectedReplyCode.charAt(0)) {
            return true;
        }

        return false;
    }

    void logger(String msg, boolean command) {
        if (msg.startsWith(PASSWORD_MESSAGE)) {
            msg = PASSWORD_MESSAGE + " ********";
        }

        else if (msg.startsWith(ACCT_MESSAGE)) {
            msg = ACCT_MESSAGE + " ********";
        }

        logger.debug(msg);
    }

}
