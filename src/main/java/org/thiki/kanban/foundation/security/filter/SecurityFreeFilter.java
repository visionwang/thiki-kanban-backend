package org.thiki.kanban.foundation.security.filter;


import javax.servlet.*;
import java.io.IOException;

/**
 * Created by xubt on 7/10/16.
 */
public class SecurityFreeFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}