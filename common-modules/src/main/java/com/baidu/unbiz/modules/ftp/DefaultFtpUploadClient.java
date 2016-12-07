/**
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.ftp;

import java.io.IOException;

import com.baidu.unbiz.biz.result.Result;
import com.baidu.unbiz.biz.result.ResultSupport;
import com.baidu.unbiz.common.logger.LoggerSupport;
import com.baidu.unbiz.modules.ftp.exception.FtpException;

/**
 * DefaultFtpClient
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年5月31日 下午4:26:07
 */
public class DefaultFtpUploadClient extends LoggerSupport implements FtpUploadClient {

    private FtpClient ftpClient;

    @Override
    public Result<Boolean> connect() {
        Result<Boolean> ret = ResultSupport.create();
        try {
            long start = System.currentTimeMillis();
            ftpClient.connect();
            ret.setSuccess(ftpClient.connected());
            long end = System.currentTimeMillis();
            ret.setRuntime(end - start);
        } catch (FtpException e) {
            logger.error("connect error", e);
            ret.setError(e);
        } catch (IOException e) {
            logger.error("connect error", e);
            ret.setError(e);
        }

        return ret;
    }

    @Override
    public Result<String> uploadFile(String filePath, String remoteFilePath) {
        return uploadFile(filePath, remoteFilePath, false);
    }

    @Override
    public Result<String> uploadFile(String filePath, String remoteFilePath, boolean append) {
        Result<String> ret = ResultSupport.create();
        try {
            long start = System.currentTimeMillis();
            String remoteFile = ftpClient.put(filePath, remoteFilePath, append);
            ret.setResult(remoteFile).setSuccess(true);
            long end = System.currentTimeMillis();
            ret.setRuntime(end - start);
        } catch (FtpException e) {
            logger.error("connect error", e);
            ret.setError(e);
        } catch (IOException e) {
            logger.error("connect error", e);
            ret.setError(e);
        }

        return ret;
    }

    @Override
    public Result<String> uploadFile(String filePath, String remoteDir, String remoteFile) {
        return uploadFile(filePath, remoteDir, remoteFile, false);
    }

    @Override
    public Result<String> uploadFile(String filePath, String remoteDir, String remoteFile, boolean append) {
        Result<String> ret = ResultSupport.create();
        try {
            long start = System.currentTimeMillis();
            ftpClient.mkdir(remoteDir);
            // FIXME
            String file = ftpClient.put(filePath, remoteDir + "/" + remoteFile, append);
            ret.setResult(file).setSuccess(true);
            long end = System.currentTimeMillis();
            ret.setRuntime(end - start);
        } catch (FtpException e) {
            logger.error("connect error", e);
            ret.setError(e);
        } catch (IOException e) {
            logger.error("connect error", e);
            ret.setError(e);
        }

        return ret;
    }

    @Override
    public Result<Boolean> disconnect() {
        Result<Boolean> ret = ResultSupport.create();
        try {
            long start = System.currentTimeMillis();
            ftpClient.disconnect();
            ret.setSuccess(!ftpClient.connected());
            long end = System.currentTimeMillis();
            ret.setRuntime(end - start);
        } catch (FtpException e) {
            logger.error("connect error", e);
            ret.setError(e);
        } catch (IOException e) {
            logger.error("connect error", e);
            ret.setError(e);
        }

        return ret;
    }

    public void setFtpClient(FtpClient ftpClient) {
        this.ftpClient = ftpClient;
    }

}
