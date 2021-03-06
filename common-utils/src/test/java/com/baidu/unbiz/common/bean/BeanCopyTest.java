/**
 * 
 */
package com.baidu.unbiz.common.bean;

import org.junit.Assert;
import org.junit.Test;

import com.baidu.unbiz.common.bean.sample.BeanSampleA;
import com.baidu.unbiz.common.bean.sample.AnnoBean;
import com.baidu.unbiz.common.bean.sample.BeanSampleC;
import com.baidu.unbiz.common.logger.CachedLogger;

/**
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2014年9月19日 上午2:09:46
 */
public class BeanCopyTest extends CachedLogger {

    @Test
    public void copyProperties() {
        BeanSampleA bean = new BeanSampleA();
        bean.setFooProp("ggg");
        BeanSampleA acopied = new BeanSampleA();
        BeanCopy.copyProperties(bean, acopied);
        Assert.assertEquals("ggg", acopied.getFooProp());

        BeanSampleC src = new BeanSampleC();
        src.setS2("11111");
        src.setS3("aaaaa");
        BeanSampleC dest = new BeanSampleC();
        BeanCopy.copyProperties(src, dest);
        Assert.assertEquals("aaaaa", dest.getS3());
        logger.info(dest);
    }

    @Test
    public void copyByMethod() {
        BeanSampleA bean = new BeanSampleA();
        bean.setFooProp("ggg");
        BeanSampleA acopied = new BeanSampleA();
        BeanCopy.copyByMethod(bean, acopied);
        Assert.assertEquals("ggg", acopied.getFooProp());

        BeanSampleC src = new BeanSampleC();
        src.setS2("11111");
        src.setS3("aaaaa");
        BeanSampleC dest = new BeanSampleC();
        BeanCopy.copyByMethod(src, dest);
        Assert.assertEquals("aaaaa", dest.getS3());
        logger.info(dest);

    }

    @Test
    public void copyByAnnotation() {
        AnnoBean bean = new AnnoBean();
        bean.setCompany(new StringBuilder("comp"));
        bean.setTest1(11);
        bean.setTest2(22);
        bean.setTest3("33");
        bean.setTest4("44");
        AnnoBean acopied = new AnnoBean();
        BeanCopy.copyByAnnotation(bean, acopied);
        Assert.assertNull(null, acopied.getTest3());
        Assert.assertEquals("44", acopied.getTest4());

        logger.info(acopied);
    }

}
