package com.baidu.unbiz.modules.routing;

/**
 * 总是选择第一个服务地址
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014-12-25 下午2:37:50
 */
public class ChooseFirst extends SchedulingSupport {

    @Override
    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public int next() {
        return 0;
    }

    @Override
    public SchedulingStrategy getStrategy() {
        return SchedulingStrategy.CHOOSE_FIRST;
    }

}
