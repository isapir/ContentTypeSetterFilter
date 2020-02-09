package net.twentyonesolutions;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/**
 * This filter sets the Content-Type header to the supplied init-param value.
 *
 * Example configuration:
 *
 * <filter>
 *   <filter-name>XmlContentTypeSetterFilter</filter-name>
 *   <filter-class>net.twentyonesolutions.ContentTypeSetterFilter</filter-class>
 *   <init-param>
 *     <param-name>contentType</param-name>
 *     <param-value>application/xml; charset=utf-8</param-value>
 *   </init-param>
 *   <init-param>
 *     <param-name>debug</param-name>
 *     <param-value>false</param-value>
 *   </init-param>
 * </filter>
 * <filter-mapping>
 *   <filter-name>XmlContentTypeSetterFilter</filter-name>
 *   <url-pattern>/rest/rss/*</url-pattern>
 * </filter-mapping>
 */
public class ContentTypeSetterFilter implements Filter {

    public static final String INIT_PARAM_CONTENT_TYPE = "contentType";
    public static final String INIT_PARAM_DEBUG = "debug";
    public static final String HEADER_NAME_CONTENT_TYPE = "Content-Type";

    String contentType = "";
    boolean isDebug = false;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        String param = filterConfig.getInitParameter(INIT_PARAM_CONTENT_TYPE);
        if (param == null) {
            throw new IllegalArgumentException(
                    String.format("Required filter init-param [%s] was not set in the deployment descriptor",
                            INIT_PARAM_CONTENT_TYPE)
            );
        }
        contentType = param;

        param = filterConfig.getInitParameter(INIT_PARAM_DEBUG);
        if ("true".equalsIgnoreCase(param) || "yes".equalsIgnoreCase(param) || "1".equals(param)) {
            isDebug = true;
        }
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        if (servletResponse instanceof HttpServletResponse) {

            filterChain.doFilter(servletRequest,
                new HttpServletResponseWrapper((HttpServletResponse) servletResponse) {

                    @Override
                    public void setContentType(String type) {
                        log(String.format("%s replacing [%s] with [%s]", "setContentType", type, contentType));
                        super.setContentType(contentType);
                    }

                    @Override
                    public void addHeader(String name, String value) {

                        if (name.equalsIgnoreCase(HEADER_NAME_CONTENT_TYPE)) {
                            log(String.format("%s replacing [%s] with [%s]", "addHeader", value, contentType));
                            super.addHeader(name, contentType);
                        }
                        else {
                            super.addHeader(name, value);
                        }
                    }

                    @Override
                    public void setHeader(String name, String value) {

                        if (name.equalsIgnoreCase(HEADER_NAME_CONTENT_TYPE)) {
                            log(String.format("%s replacing [%s] with [%s]", "setHeader", value, contentType));
                            super.setHeader(name, contentType);
                        }
                        else {
                            super.setHeader(name, value);
                        }
                    }

                    void log(String message) {
                        if (!isDebug)
                            return;

                        System.err.println(this.getClass().getName() + ": " + message);
                    }
                }
            );
        }
        else {

            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
