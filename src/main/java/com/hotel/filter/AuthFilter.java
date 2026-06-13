package com.hotel.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String contextPath = httpRequest.getContextPath();
        String uri = httpRequest.getRequestURI();

        boolean isPublic = uri.equals(contextPath + "/")
                || uri.equals(contextPath + "/login")
                || uri.startsWith(contextPath + "/assets/");

        HttpSession session = httpRequest.getSession(false);
        boolean loggedIn = session != null && session.getAttribute("adminUser") != null;

        if (isPublic || loggedIn) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(contextPath + "/login");
        }
    }

    @Override
    public void destroy() {
    }
}
