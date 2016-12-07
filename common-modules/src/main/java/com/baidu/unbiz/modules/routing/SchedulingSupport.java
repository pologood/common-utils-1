/**
 * Copyright (C) 2014 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.routing;

/**
 * 路由调度的简单支持
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014-12-25 下午4:11:46
 */
public abstract class SchedulingSupport implements Scheduling {

    /**
     * 路由总数
     */
    protected int total = 1;

    @Override
    public int getTotal() {
        return total;
    }

}
