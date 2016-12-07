/**
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.ftp;

/**
 * FtpClientConstant
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年5月31日 下午4:50:32
 */
public interface FtpClientConstant {

    int DEFAULT_MONITOR_INTERVAL = 65535;

    int DEFAULT_BUFFER_SIZE = 16384;

    int MAX_PORT = 65535;

    int DEFAULT_TIMEOUT = 60 * 1000;

    int DEFAULT_RETRY_COUNT = 3;

    int DEFAULT_RETRY_DELAY = 5000;

    int DEFAULT_TCP_BUFFER_SIZE = 128 * 1024;

    String DEFAULT_ENCODING = "US-ASCII";

    byte CARRIAGE_RETURN = 13;

    byte LINE_FEED = 10;

    byte[] FTP_LINE_SEPARATOR = { CARRIAGE_RETURN, LINE_FEED };

    int SHORT_TIMEOUT = 500;

    String STORE_CMD = "STOR ";

    String STOU_FILENAME_MARKER = "FILE:";

    String EOL = "\r\n";

    int MAX_ACTIVE_RETRY = 100;

    int DEFAULT_PORT = 21;

    String DEBUG_ARROW = "---> ";

    String PASSWORD_MESSAGE = DEBUG_ARROW + "PASS";

    String ACCT_MESSAGE = DEBUG_ARROW + "ACCT";

}
