/**
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.ftp.exception;

/**
 * FtpTransferCancelledException
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年6月1日 下午2:48:43
 */
public class FtpTransferCancelledException extends FtpException {

    private static final long serialVersionUID = 6691844502433993540L;

    private static final String message = "Transfer was cancelled";

    public FtpTransferCancelledException() {
        super(message);
    }

}
