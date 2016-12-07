/**
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.ftp;

import com.baidu.unbiz.biz.result.Result;

/**
 * FtpClient
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年5月25日 下午4:49:41
 */
public interface FtpUploadClient {

    Result<Boolean> connect();

    Result<String> uploadFile(String filePath, String remoteFilePath);

    Result<String> uploadFile(String filePath, String remoteFilePath, boolean append);

    Result<String> uploadFile(String filePath, String remoteDir, String remoteFile);

    Result<String> uploadFile(String filePath, String remoteDir, String remoteFile, boolean append);

    Result<Boolean> disconnect();

}
