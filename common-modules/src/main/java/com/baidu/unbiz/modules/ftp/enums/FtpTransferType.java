/**
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.ftp.enums;

import com.baidu.unbiz.common.able.Valuable;

/**
 * FtpTransferType
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年5月31日 下午4:56:23
 */
public enum FtpTransferType implements Valuable<String> {
    ASCII("A"), BINARY("I");

    private String type;

    FtpTransferType(String type) {
        this.type = type;
    }

    @Override
    public String value() {
        return type;
    }
}
