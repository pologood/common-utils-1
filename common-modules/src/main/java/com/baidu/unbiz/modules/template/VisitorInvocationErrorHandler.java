package com.baidu.unbiz.modules.template;

/**
 * 如果visitor实现了这个接口，那么当访问visitor方法出错时，接口将被调用，以处理异常。否则，<code>Template</code> 将抛出异常。
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2015-2-5 上午10:22:50
 */
public interface VisitorInvocationErrorHandler {
    /**
     * 异常处理
     * 
     * @param desc 异常描述
     * @param e 异常
     * @throws Exception
     */
    void handleInvocationError(String desc, Throwable e) throws Exception;
}
