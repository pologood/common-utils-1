package com.baidu.unbiz.modules.webpage;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import org.junit.Test;

import com.baidu.unbiz.common.test.TestUtil;
import com.baidu.unbiz.modules.webpage.PageComponent;
import com.baidu.unbiz.modules.webpage.PageComponentRegistry;
import com.baidu.unbiz.modules.webpage.RequestProcessor;
import com.baidu.unbiz.modules.webpage.simple.SimpleComponent;

/**
 * PageComponentTests
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年3月20日 下午11:32:49
 */
public class PageComponentTest extends AbstractRequestProcessorTest {
    @Test
    public void getComponentPaths() {
        MyProcessor processor = new MyProcessor();
        assertArrayEquals(new String[] { "simple/" }, processor.getComponentPaths());

        new SimpleComponent(processor, "a");
        new SimpleComponent(processor, "x/c");
        new SimpleComponent(processor, "x/b");
        new SimpleComponent(processor, "x/b/c");
        new SimpleComponent(processor, "x/b/d");

        // 注意排序
        assertArrayEquals(new String[] { "simple/", "x/b/c/", "x/b/d/", "x/b/", "x/c/", "a/" },
                processor.getComponentPaths());
    }

    @Test
    public void dupComponent() {
        MyProcessor processor = new MyProcessor();

        try {
            new SimpleComponent(processor, "simple");
            fail();
        } catch (IllegalArgumentException e) {
            assertThat(e, TestUtil.exception("duplicated component", "simple"));
        }
    }

    @Test
    public void getComponent() {
        MyProcessor processor = new MyProcessor();
        SimpleComponent sc = processor.getComponent("simple", SimpleComponent.class);
        assertSame(processor.simple, sc);

        // without type
        sc = processor.getComponent("simple", null);
        assertSame(processor.simple, sc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getComponent_notFound() {
        MyProcessor processor = new MyProcessor();
        processor.getComponent("notFound", SimpleComponent.class);
    }

    @Test(expected = ClassCastException.class)
    public void getComponent_wrongType() {
        MyProcessor processor = new MyProcessor();

        class OtherComponent extends PageComponent {
            public OtherComponent(PageComponentRegistry registry, String componentPath) {
                super(registry, componentPath);
            }
        }

        processor.getComponent("simple", OtherComponent.class);
    }

    @Test
    public void componentPath() throws Exception {
        MyProcessor processor = new MyProcessor();

        processor.processRequest(new MyRequest("http://localhost", ""));

        assertEquals("text/plain", contentType);
        assertEquals("simple template: http://localhost/simple/test.gif", sw.toString());
    }

    @Test
    public void findComponentResource() throws Exception {
        MyProcessor processor = new MyProcessor();

        processor.processRequest(new MyRequest("http://localhost", "/simple/test.gif"));

        assertEquals("image/gif", contentType);
        assertEquals("i'm gif", new String(baos.toByteArray()));
    }

    @Test
    public void findComponentResource_fallback() throws Exception {
        MyProcessor processor = new MyProcessor();

        processor.processRequest(new MyRequest("http://localhost", "/simple/dummy2.txt"));

        assertEquals("text/plain", contentType);
        assertEquals("dummy2", new String(baos.toByteArray()));
    }

    @Test
    public void findRawResource() throws Exception {
        MyProcessor processor = new MyProcessor();

        processor.processRequest(new MyRequest("http://localhost", "/dummy2.txt"));

        assertEquals("text/plain", contentType);
        assertEquals("dummy2", new String(baos.toByteArray()));
    }

    @Test
    public void getComponentResources() {
        MyProcessor processor = new MyProcessor();

        List<String> css = processor.getComponentResources("css");

        assertArrayEquals(new Object[] { "simple/simple.css" }, css.toArray());

        List<String> js = processor.getComponentResources(".js");

        assertArrayEquals(new Object[] {}, js.toArray());
    }

    private static class MyProcessor extends RequestProcessor<MyRequest> {
        private final SimpleComponent simple = new SimpleComponent(this, "simple");

        @Override
        protected void renderPage(MyRequest request, String resourceName) throws IOException {
            PrintWriter out = request.getWriter("text/plain");

            simple.visitTemplate(out, request);
        }

        @Override
        protected boolean resourceExists(String resourceName) {
            return true;
        }
    }
}
