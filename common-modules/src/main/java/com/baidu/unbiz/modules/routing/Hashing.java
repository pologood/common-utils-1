package com.baidu.unbiz.modules.routing;

/**
 * 通过请求的哈希值进行路由
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014-12-25 下午2:56:30
 */
public class Hashing extends SchedulingSupport {

    /**
     * 维护当前的请求对象，用于生成哈希值
     */
    private final ThreadLocal<Object> local = new ThreadLocal<Object>();

    @Override
    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public int next() {
        try {
            Object object = local.get();
            if (object == null) {
                return 0;
            }
            int next = object.hashCode() % total;
            return next;
        } finally {
            local.remove();
        }
    }

    /**
     * 设置当前请求对象 FIXME
     * 
     * @param value 请求对象
     */
    public void setObject(Object value) {
        local.set(value);
    }

    @Override
    public SchedulingStrategy getStrategy() {
        return SchedulingStrategy.HASH;
    }

}
