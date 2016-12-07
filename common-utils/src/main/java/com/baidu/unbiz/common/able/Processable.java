/**
 * 
 */
package com.baidu.unbiz.common.able;

/**
 * 代表一个系统进程
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年7月31日 下午8:16:07
 */
public interface Processable extends Closure {

    /**
     * info about execute results
     * 
     * @return execute results
     */
    String getInfo();

    /**
     * 执行命令
     * 
     * @param input 命令参数集
     */
    void execute(String...inputs);

    /**
     * 执行命令
     * 
     * @param input 命令参数
     */
    void execute(String input);

    /**
     * 是否执行成功 FIXME
     * 
     * @return 是否执行成功
     */
    boolean isSucces();

}
