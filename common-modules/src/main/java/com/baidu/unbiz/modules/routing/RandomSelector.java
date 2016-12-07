/**
 * Copyright (C) 2014 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.modules.routing;

import java.util.Random;

import com.baidu.unbiz.common.RandomUtil;

/**
 * 随机选择路由
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014-12-25 下午3:09:20
 */
public class RandomSelector extends SchedulingSupport {

    /**
     * 随机生成器
     */
    private Random random = new Random(RandomUtil.next(1, Integer.MAX_VALUE));

    @Override
    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public int next() {
        int next = RandomUtil.next(random, 0, total - 1);

        return next;
    }

    @Override
    public SchedulingStrategy getStrategy() {
        return SchedulingStrategy.RANDOM;
    }

}
