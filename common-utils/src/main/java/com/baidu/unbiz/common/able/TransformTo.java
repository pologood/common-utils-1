/**
 * 
 */
package com.baidu.unbiz.common.able;

/**
 * 转换器
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年9月22日 下午11:23:24
 */
public interface TransformTo<TO> {

    /**
     * 将对象转换成<tt>TO</tt>类型
     * 
     * @return 转换后的结果
     */
    TO transform();

}
