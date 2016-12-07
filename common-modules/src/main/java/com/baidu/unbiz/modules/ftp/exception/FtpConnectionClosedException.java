/**
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.ftp.exception;

/**
 * FtpConnectionClosedException
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年5月31日 下午7:15:51
 */
public class FtpConnectionClosedException extends FtpException {

    private static final long serialVersionUID = 7191818747409035710L;

    public FtpConnectionClosedException(String msg) {
        super(msg);
    }

}
