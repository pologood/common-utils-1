package com.baidu.unbiz.modules.webpage.myprocessor;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.junit.Before;
import org.junit.Test;

import com.baidu.unbiz.common.CollectionUtil;
import com.baidu.unbiz.common.StringUtil;
import com.baidu.unbiz.modules.webpage.RequestProcessor;
import com.baidu.unbiz.modules.webpage.ServletRequestContext;
import com.meterware.httpunit.WebResponse;
import com.meterware.servletunit.InvocationContext;
import com.meterware.servletunit.ServletRunner;
import com.meterware.servletunit.ServletUnitClient;

/**
 * LastModifiedTests
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年3月20日 下午11:33:20
 */
public class LastModifiedTest {
    private SimpleDateFormat fmt;
    private ServletUnitClient client;
    private InvocationContext invocationContext;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletContext servletContext;
    private Map<String, String> textResources;
    private MyProcessor page;

    @Before
    public void init() throws Exception {
        fmt = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US);
        fmt.setTimeZone(TimeZone.getTimeZone("GMT"));

        ServletRunner servletRunner = new ServletRunner();
        servletRunner.registerServlet("/myservlet/*", MyServlet.class.getName());

        client = servletRunner.newClient();
        client.setExceptionsThrownOnErrorStatus(false);
        client.getClientProperties().setAutoRedirect(false);

        invocationContext = client.newInvocation("http://localhost/myservlet");

        request = invocationContext.getRequest();

        // http unit在这里有个可耻的错误，HH:mm:ss写成了hh:mm:ss，导致日期错误。
        response = new HttpServletResponseWrapper(invocationContext.getResponse()) {
            @Override
            public void setDateHeader(String name, long date) {
                super.setHeader(name, fmt.format(new Date(date)));
            }
        };
        servletContext = invocationContext.getServlet().getServletConfig().getServletContext();

        textResources = CollectionUtil.createHashMap();

        textResources.put("file1", "file1 content");
        textResources.put("path/to/", "list");
        textResources.put("path/to/file2", "file2 content");

        page = new MyProcessor();
    }

    @Test
    public void lastModifiedResource() throws Exception {
        page.processRequest(getRequestContext("/dummy.txt"));

        assertEquals(new File(getClass().getResource("dummy.txt").toURI()).lastModified() / 1000 * 1000,
                getResponseLastModified());

        page.processRequest(getRequestContext("/prototype.js"));

        assertEquals(new File(RequestProcessor.class.getResource("prototype.js").toURI()).lastModified() / 1000 * 1000,
                getResponseLastModified());
    }

    @Test
    public void lastModifiedResourceTemplate() throws Exception {
        page.processRequest(getRequestContext("/style.txt"));
        assertEquals(-1, getResponseLastModified());
    }

    @Test
    public void lastModifiedListRedirect() throws Exception {
        page.processRequest(getRequestContext("/path/to"));
        assertEquals(-1, getResponseLastModified());
    }

    @Test
    public void lastModifiedRenderList() throws Exception {
        page.processRequest(getRequestContext("/path/to/"));
        assertEquals(-1, getResponseLastModified());
    }

    @Test
    public void lastModifiedNotFound() throws Exception {
        page.processRequest(getRequestContext("/path/to/notFound"));
        assertEquals(-1, getResponseLastModified());
    }

    private ServletRequestContext getRequestContext(String name) {
        return new ServletRequestContext(request, response, servletContext, "http://localhost/myservlet", name) {
        };
    }

    private long getResponseLastModified() throws Exception {
        WebResponse webResponse = client.getResponse(invocationContext);
        String lastModifiedStr = StringUtil.trimToNull(webResponse.getHeaderField("last-modified"));

        if (lastModifiedStr == null) {
            return -1;
        } else {
            return fmt.parse(lastModifiedStr).getTime();
        }
    }

    private class MyProcessor extends RequestProcessor<ServletRequestContext> {
        @Override
        protected void renderPage(ServletRequestContext request, String resourceName) throws IOException {
            PrintWriter out = request.getWriter("text/plain");
            out.println(textResources.get(resourceName));
        }

        @Override
        protected boolean resourceExists(String resourceName) {
            return textResources.containsKey(resourceName);
        }
    }

    public static class MyServlet extends HttpServlet {
        private static final long serialVersionUID = 4555315413735967231L;
    }
}
