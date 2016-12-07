package com.baidu.unbiz.modules.bus.pool;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.baidu.unbiz.modules.pool.FileWriteExecutor;

/**
 * 
 * @author <a href="mailto:xuc@iphonele.com">saga67</a>
 * 
 * @version create on 2012-3-20 上午10:44:37
 */
@ContextConfiguration(locations = { "classpath:spring/applicationContext.xml" })
public class FileWriteExecutorTestCase extends AbstractJUnit4SpringContextTests {

	@Resource
	private FileWriteExecutor executor;

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGenFile() {
		List<String> strings = new ArrayList<String>();
		byte[] data = new byte[1024];
		for (int i = 0; i < 1024; i++) {
			data[i] = (byte) i;
		}
		strings.add(new String(data));

		for (int i = 0; i < 64; i++) {
			executor.execute(strings);
		}

		/*
		 * int fileSize = FileUtil.getFileSize("/tmp/" + DateUtil.formatDate(new
		 * Date(), "yyyy-MM-dd") + File.separator + "test.executor.0");
		 * System.out.println("file 0:" + fileSize);
		 * 
		 * fileSize = FileUtil.getFileSize("/tmp/" + DateUtil.formatDate(new
		 * Date(), "yyyy-MM-dd") + File.separator + "test.executor.1");
		 * System.out.println("file 1:" + fileSize);
		 */
	}
}
