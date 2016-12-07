/**
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.ftp;

import java.io.IOException;

import org.junit.Test;

import com.baidu.unbiz.biz.result.Result;
import com.baidu.unbiz.common.logger.LoggerSupport;
import com.baidu.unbiz.modules.ftp.exception.FtpException;

import org.junit.Assert;

/**
 * FtpUploadClientTest
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年6月14日 下午4:50:49
 */
public class FtpUploadClientTest extends LoggerSupport {

    @Test
    public void testConnect() {
        DefaultFtpUploadClient fuc = new DefaultFtpUploadClient();
        FtpClient client = new DefaultFtpClient();
        try {
            client.setRemoteHost("54.179.153.105");
            client.setUsername("testftp");
            client.setPassword("testftp");
            fuc.setFtpClient(client);
            Result<Boolean> ret = fuc.connect();
            Assert.assertTrue(ret.isSuccess());
            logger.info("client connect spends {}ms", ret.getRuntime());
            Result<Boolean> ret2 = fuc.disconnect();
            Assert.assertTrue(ret2.isSuccess());
            logger.info("client disconnect spends {}ms", ret2.getRuntime());
        } catch (FtpException e) {
            logger.error("ftp exception", e);
        } catch (IOException e) {
            logger.error("io exception", e);
        }
    }

    @Test
    public void testUpload() {
        DefaultFtpUploadClient fuc = new DefaultFtpUploadClient();
        FtpClient client = new DefaultFtpClient();
        try {
            client.setRemoteHost("54.179.153.105");
            client.setUsername("testftp");
            client.setPassword("testftp");
            fuc.setFtpClient(client);
            fuc.connect();
            Result<String> ret = fuc.uploadFile("D:/backup/goagnt.rar", "2016/goagnt2.rar");
            Assert.assertTrue(ret.isSuccess());
            logger.info("client upload to {} spends {}ms", ret.getResult(), ret.getRuntime());
            fuc.disconnect();
        } catch (FtpException e) {
            logger.error("ftp exception", e);
        } catch (IOException e) {
            logger.error("io exception", e);
        }
    }

    @Test
    public void testDirUpload() {
        DefaultFtpUploadClient fuc = new DefaultFtpUploadClient();
        FtpClient client = new DefaultFtpClient();
        try {
            client.setRemoteHost("54.179.153.105");
            client.setUsername("testftp");
            client.setPassword("testftp");
            fuc.setFtpClient(client);
            fuc.connect();
            Result<String> ret = fuc.uploadFile("D:/backup/goagnt.rar", "aa/bb/cc", "goagnt2.rar");
            Assert.assertTrue(ret.isSuccess());
            logger.info("client upload to {} spends {}ms", ret.getResult(), ret.getRuntime());
            fuc.disconnect();
        } catch (FtpException e) {
            logger.error("ftp exception", e);
        } catch (IOException e) {
            logger.error("io exception", e);
        }
    }

}
