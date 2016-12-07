/**
 * 
 */
package com.baidu.unbiz.common.logger;

/**
 * 增加对象的直接打印，方便编码
 * 
 * @author <a href="mailto:xuc@iphonele.com">saga67</a>
 * 
 * @version create on 2013年8月13日 下午9:29:13
 */
public interface Logger extends org.slf4j.Logger {

    // ==========================================================================
    // 打印对象。
    // ==========================================================================
    void trace(Object bean);

    void info(Object bean);

    void debug(Object bean);

    void warn(Object bean);

    void error(Object bean);

    // ==========================================================================
    // add。
    // ==========================================================================
    void error(String msg, Throwable t, Object...arguments);

    // ==========================================================================
    // 以下为兼容低版本slf4j，进行重写
    // ==========================================================================
    void trace(String format, Object...args);

    void info(String format, Object...args);

    void debug(String format, Object...args);

    void warn(String format, Object...args);

    void error(String format, Object...args);

}
