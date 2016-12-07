/**
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.ftp;

import java.io.IOException;

import org.junit.Test;

import com.baidu.unbiz.common.logger.LoggerSupport;
import com.baidu.unbiz.modules.ftp.exception.FtpException;

/**
 * FtpClientTest
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年6月14日 下午4:50:49
 */
public class FtpClientTest extends LoggerSupport {

    @Test
    public void testConnect() {
        FtpClient client = new DefaultFtpClient();
        try {
            client.setRemoteHost("54.179.153.105");
            client.setUsername("testftp");
            client.setPassword("testftp");
            client.connect();
            logger.info("client connected={}", client.connected());
            client.disconnect();
        } catch (FtpException e) {
            logger.error("ftp exception", e);
        } catch (IOException e) {
            logger.error("io exception", e);
        }
    }

    @Test
    public void testUpload() {
        FtpClient client = new DefaultFtpClient();
        try {
            client.setRemoteHost("54.179.153.105");
            client.setUsername("testftp");
            client.setPassword("testftp");
            client.connect();
            client.put("D:/backup/goagnt.rar", "goagnt2.rar");
            client.disconnect();
        } catch (FtpException e) {
            logger.error("ftp exception", e);
        } catch (IOException e) {
            logger.error("io exception", e);
        }
    }
    
    @Test
    public void testMkUpload() {
        FtpClient client = new DefaultFtpClient();
        try {
            client.setRemoteHost("54.179.153.105");
            client.setUsername("testftp");
            client.setPassword("testftp");
            client.connect();
            client.mkdir("yyy/zzz");
            client.put("D:/backup/goagnt.rar", "yyy/zzz/goagnt2.rar");
            client.disconnect();
        } catch (FtpException e) {
            logger.error("ftp exception", e);
        } catch (IOException e) {
            logger.error("io exception", e);
        }
    }

}
