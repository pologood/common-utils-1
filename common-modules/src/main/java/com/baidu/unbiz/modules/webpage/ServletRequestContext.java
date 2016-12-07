
package com.baidu.unbiz.modules.webpage;

import static com.baidu.unbiz.common.Assert.assertNotNull;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基于servlet api的request context实现。
 * 
 * @author <a href="mailto:xuchen06@baidu.com">xuc</a>
 * @version create on 2016年4月27日 下午3:36:24
 */
public class ServletRequestContext extends RequestContext {
    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final ServletContext servletContext;

    public ServletRequestContext(HttpServletRequest request, HttpServletResponse response,
            ServletContext servletContext) {
        super(ServletUtil.getServletBaseURL(request), ServletUtil.getServletResourcePath(request));

        this.request = assertNotNull(request, "request is null");
        this.response = assertNotNull(response, "response is null");
        this.servletContext = assertNotNull(servletContext, "servletContext is null");
    }

    public ServletRequestContext(HttpServletRequest request, HttpServletResponse response,
            ServletContext servletContext, String baseURL, String resourceName) {
        super(baseURL, resourceName);

        this.request = assertNotNull(request, "request is null");
        this.response = assertNotNull(response, "response is null");
        this.servletContext = assertNotNull(servletContext, "servletContext is null");
    }

    public final HttpServletRequest getRequest() {
        return request;
    }

    public final HttpServletResponse getResponse() {
        return response;
    }

    public final ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    protected OutputStream doGetOutputStream(String contentType) throws IOException {
        response.setContentType(contentType);
        return response.getOutputStream();
    }

    @Override
    protected PrintWriter doGetWriter(String contentType) throws IOException {
        response.setContentType(contentType);
        return response.getWriter();
    }

    @Override
    public final void redirectTo(String location) throws IOException {
        response.sendRedirect(location);
    }

    /** 当请求的资源找不到时，方法被调用。子类可以修改此行为。 */
    @Override
    public void resourceNotFound(String resourceName) throws IOException {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Resource Not Found: " + resourceName);
    }

    /** 取得servlet request context。 */
    public static ServletRequestContext getServletRequestContext(RequestContext request) {
        if (request instanceof ServletRequestContext) {
            return (ServletRequestContext) request;
        } else {
            return null;
        }
    }

    /** 设置http headers，禁用cache。 */
    public static void disableCache(RequestContext request) {
        if (getServletRequestContext(request) != null) {
            HttpServletResponse response = getServletRequestContext(request).getResponse();

            // Set to expire far in the past.
            response.setHeader("Expires", "Sat, 6 May 1911 12:00:00 GMT");

            // Set standard HTTP/1.1 no-cache headers.
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");

            // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
            response.addHeader("Cache-Control", "post-check=0, pre-check=0");

            // Set standard HTTP/1.0 no-cache header.
            response.setHeader("Pragma", "no-cache");
        }
    }
}
