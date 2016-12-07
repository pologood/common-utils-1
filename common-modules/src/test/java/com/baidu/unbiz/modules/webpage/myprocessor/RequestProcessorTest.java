package com.baidu.unbiz.modules.webpage.myprocessor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.common.test.TestUtil;
import com.baidu.unbiz.modules.webpage.AbstractRequestProcessorTest;
import com.baidu.unbiz.modules.webpage.RequestContext;
import com.baidu.unbiz.modules.webpage.RequestProcessor;

/**
 * RequestProcessorTests
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年3月20日 下午11:33:55
 */
public class RequestProcessorTest extends AbstractRequestProcessorTest {
    private Map<String, String> textResources;
    private MyProcessor page;

    @Before
    public void init() {
        textResources = CollectionUtil.createHashMap();

        textResources.put("file1", "file1 content");
        textResources.put("path/to/", "list");
        textResources.put("path/to/file2", "file2 content");

        page = new MyProcessor();
    }

    @Test
    public void requestContext_getWriter() throws IOException {
        RequestContext context = new MyRequest("http://localhost", "/dummy.txt");

        try {
            context.getWriter();
            fail();
        } catch (IllegalStateException e) {
            assertThat(e, TestUtil.exception("call getWriter(contentType) first"));
        }

        PrintWriter writer = context.getWriter("text/plain");

        assertSame(writer, context.getWriter());
        assertSame(writer, context.getWriter("text/plain"));
    }

    @Test
    public void requestContext_getOutputStream() throws IOException {
        RequestContext context = new MyRequest("http://localhost", "/dummy.txt");

        try {
            context.getOutputStream();
            fail();
        } catch (IllegalStateException e) {
            assertThat(e, TestUtil.exception("call getOutputStream(contentType) first"));
        }

        OutputStream stream = context.getOutputStream("text/plain");

        assertSame(stream, context.getOutputStream());
        assertSame(stream, context.getOutputStream("text/plain"));
    }

    @Test
    public void renderResource() throws IOException {
        page.processRequest(new MyRequest("http://localhost", "/dummy.txt"));

        assertEquals("text/plain", contentType);
        assertEquals("dummy", new String(baos.toByteArray()).trim());
        assertEquals(null, redirectUrl);
        assertEquals(null, resourceNotFound);

        // actually located at superclass' package
        page.processRequest(new MyRequest("http://localhost", "/prototype.js"));

        assertEquals("application/javascript", contentType);
        assertTrue(baos.toByteArray().length > 0);
        assertEquals(null, redirectUrl);
        assertEquals(null, resourceNotFound);
    }

    @Test
    public void renderResourceTemplate() throws IOException {
        page.processRequest(new MyRequest("http://localhost", "/style.txt"));

        assertEquals("text/plain", contentType);
        assertEquals("http://localhost/style.css", sw.toString().trim());
        assertEquals(null, redirectUrl);
        assertEquals(null, resourceNotFound);
    }

    @Test
    public void renderListRedirect() throws IOException {
        page.processRequest(new MyRequest("http://localhost", "/path/to"));

        assertEquals(null, contentType);
        assertEquals("http://localhost/path/to/", redirectUrl);
        assertEquals(null, resourceNotFound);
    }

    @Test
    public void renderList() throws IOException {
        page.processRequest(new MyRequest("http://localhost", "/path/to/"));

        assertEquals("text/plain", contentType);
        assertEquals("list", sw.toString().trim());
        assertEquals(null, redirectUrl);
        assertEquals(null, resourceNotFound);
    }

    @Test
    public void notFound() throws IOException {
        page.processRequest(new MyRequest("http://localhost", "/path/to/notFound"));

        assertEquals(null, contentType);
        assertEquals(null, redirectUrl);
        assertEquals("path/to/notFound", resourceNotFound);
    }

    private class MyProcessor extends RequestProcessor<MyRequest> {
        @Override
        protected void renderPage(MyRequest request, String resourceName) throws IOException {
            PrintWriter out = request.getWriter("text/plain");
            out.println(textResources.get(resourceName));
        }

        @Override
        protected boolean resourceExists(String resourceName) {
            return textResources.containsKey(resourceName);
        }
    }
}
