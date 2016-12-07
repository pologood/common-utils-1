/**
 * Copyright (C) 2014 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.common.source;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 会意注解，表示将来要使用的方案
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014-11-22 上午5:43:22
 */
@Retention(RetentionPolicy.SOURCE)
public @interface ForFuture {

    /**
     * 将来要使用的方案
     * 
     * @return 方案描述
     */
    String[] value();

}
