/**
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.unbiz.common.able;

import java.util.concurrent.TimeUnit;

/**
 * 阻塞线程
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-9-17 上午12:57:05
 */
public interface Blockable {

    void blockUntilActive();

    void blockUntilActive(long timeout, TimeUnit timeUnit);

}
