package com.baidu.unbiz.common.lang;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.baidu.unbiz.common.ClassLoaderUtil;
import com.baidu.unbiz.common.sample.SampleSignal;

/**
 * 
 * @author <a href="mailto:xuc@iphonele.com">saga67</a>
 * 
 * @version create on 2012-3-16 下午4:59:31
 */
public class Object2XmlTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() throws Exception {
        SampleSignal obj = new SampleSignal();
        obj.setIntField(1);
        obj.setShortField((byte) 1);
        obj.setByteField((byte) 1);
        obj.setLongField(1L);
        obj.setStringField("中文");
        obj.setByteArrayField(new byte[] { 127 });
        String outFileName = ClassLoaderUtil.getClasspath() + String.format("%08d", 1) + ".xml";
        String outFilePath = Object2Xml.object2XML(obj, outFileName);
        Object assertobj = Object2Xml.xml2Object(outFilePath);
        Assert.assertEquals(assertobj, obj);
    }
}
