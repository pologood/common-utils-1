package com.baidu.unbiz.modules.bus.pool;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.baidu.unbiz.modules.pool.DelayExecuteBuffer;

/**
 * 
 * @author <a href="mailto:xuc@iphonele.com">saga67</a>
 * 
 * @version create on 2012-3-20 上午10:44:24
 */
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
public class DelayExecuteBufferTestCase extends
		AbstractJUnit4SpringContextTests {

	@Resource
	private DelayExecuteBuffer<String> testBuffer;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBatchExecute() throws Exception {
		for (int i = 0; i < 100000; i++) {
			testBuffer.add(String.valueOf(i));
		}
	}
}
