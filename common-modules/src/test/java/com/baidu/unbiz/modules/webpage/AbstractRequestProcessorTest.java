package com.baidu.unbiz.modules.webpage;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.Before;

import com.baidu.unbiz.modules.webpage.RequestContext;

/**
 * AbstractRequestProcessorTests
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年3月20日 下午11:32:27
 */
public abstract class AbstractRequestProcessorTest {
    protected String contentType;
    protected ByteArrayOutputStream baos;
    protected StringWriter sw;
    protected String redirectUrl;
    protected String resourceNotFound;

    @Before
    public void initStream() {
        baos = new ByteArrayOutputStream();
        sw = new StringWriter();
    }

    protected class MyRequest extends RequestContext {
        public MyRequest(String baseURL, String resourceName) {
            super(baseURL, resourceName);
        }

        @Override
        protected OutputStream doGetOutputStream(String contentType) throws IOException {
            AbstractRequestProcessorTest.this.contentType = contentType;
            return new BufferedOutputStream(baos);
        }

        @Override
        protected PrintWriter doGetWriter(String contentType) throws IOException {
            AbstractRequestProcessorTest.this.contentType = contentType;
            return new PrintWriter(sw, true);
        }

        @Override
        public void redirectTo(String url) throws IOException {
            redirectUrl = url;
        }

        @Override
        public void resourceNotFound(String resourceName) throws IOException {
            resourceNotFound = resourceName;
        }
    }
}
