/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.db;

import com.baidu.unbiz.common.lang.Message;

/**
 * 数据库消息
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-13 上午12:15:15
 */
public class DatabaseMessage<T> extends Message<T> {

    /**
     * 
     */
    private static final long serialVersionUID = -1126365795517907366L;

    /**
     * 行结果元信息 @see RowMeta
     */
    private RowMeta rowMeta;

    public DatabaseMessage(T body) {
        super(body);
    }

    public DatabaseMessage(T body, RowMeta rowMeta) {
        super(body);

        this.rowMeta = rowMeta;
    }

    public RowMeta getRowMeta() {
        return rowMeta;
    }

    public void setRowMeta(RowMeta rowMeta) {
        this.rowMeta = rowMeta;
    }

}
