package com.baidu.unbiz.common.lang;

import java.util.concurrent.Callable;

import com.baidu.unbiz.common.logger.LoggerSupport;

/**
 * 记录调用时间 {@link LoggingCallback}.
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016-1-19 下午6:53:17
 */
public class Clocker extends LoggerSupport {

    private final String subject;
    private final String object;

    public Clocker(String subject, String object) {
        this.subject = subject;
        this.object = object;
    }

    public <T> T go(Callable<T> callback) throws Exception {
        long start = System.currentTimeMillis();
        try {
            return callback.call();
        } finally {
            long end = System.currentTimeMillis();
            if (logger.isInfoEnabled()) {
                logger.info("{} spent {} milliseconds to finish invocation for {}", subject, (end - start), object);
            }
        }
    }
}