package com.azumi.auth;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(filterName = "AuthFilter", urlPatterns = "/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = httpRequest.getSession(false);
        if (AuthSession.isAuthenticated(session)) {
            chain.doFilter(request, response);
            return;
        }

        String loginUrl = httpRequest.getContextPath() + "/login";
        if (!path.isEmpty() && !"/".equals(path)) {
            loginUrl += "?redirect=" + java.net.URLEncoder.encode(path, java.nio.charset.StandardCharsets.UTF_8);
        }
        httpResponse.sendRedirect(loginUrl);
    }

    private boolean isPublicPath(String path) {
        return "/login".equals(path);
    }
}
