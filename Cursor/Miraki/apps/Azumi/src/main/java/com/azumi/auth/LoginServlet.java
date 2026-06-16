package com.azumi.auth;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (AuthSession.isAuthenticated(session)) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        String error = request.getParameter("error");
        String redirect = sanitizeRedirect(request.getParameter("redirect"));
        renderPage(response, error != null, redirect);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = trim(request.getParameter("username"));
        String password = request.getParameter("password");
        String redirect = sanitizeRedirect(request.getParameter("redirect"));

        UserStore store = UserStoreListener.get(getServletContext());
        Optional<UserAccount> account = store.authenticate(username, password);

        if (account.isEmpty()) {
            String loginUrl = request.getContextPath() + "/login?error=1";
            if (!redirect.isEmpty()) {
                loginUrl += "&redirect=" + java.net.URLEncoder.encode(redirect, java.nio.charset.StandardCharsets.UTF_8);
            }
            response.sendRedirect(loginUrl);
            return;
        }

        HttpSession session = request.getSession(true);
        session.setAttribute(AuthSession.USERNAME, account.get().username());
        session.setAttribute(AuthSession.ROLE, account.get().role().getId());

        String target = redirect.isEmpty() ? request.getContextPath() + "/" : request.getContextPath() + redirect;
        response.sendRedirect(target);
    }

    private String sanitizeRedirect(String redirect) {
        if (redirect == null || redirect.isBlank() || !redirect.startsWith("/") || redirect.startsWith("//")) {
            return "";
        }
        return redirect;
    }

    private String trim(String value) {
        return value == null ? "" : value.trim();
    }

    private void renderPage(HttpServletResponse response, boolean showError, String redirect)
            throws IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang=\"en\">");
            out.println("<head>");
            out.println("  <meta charset=\"UTF-8\">");
            out.println("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
            out.println("  <title>Login - AzumiDesigns</title>");
            out.println("  <style>");
            out.println("    body { font-family: system-ui, sans-serif; margin: 0; min-height: 100vh; display: grid; place-items: center; background: #f4f4f5; color: #18181b; }");
            out.println("    .card { width: min(100%, 22rem); background: #fff; border-radius: 0.75rem; padding: 1.5rem; box-shadow: 0 10px 30px rgba(0,0,0,0.08); }");
            out.println("    h1 { margin: 0 0 0.25rem; font-size: 1.5rem; }");
            out.println("    p { margin: 0 0 1rem; color: #52525b; }");
            out.println("    label { display: block; font-size: 0.875rem; margin-bottom: 0.25rem; }");
            out.println("    input { width: 100%; box-sizing: border-box; padding: 0.625rem 0.75rem; margin-bottom: 0.875rem; border: 1px solid #d4d4d8; border-radius: 0.5rem; }");
            out.println("    button { width: 100%; padding: 0.75rem; border: 0; border-radius: 0.5rem; background: #18181b; color: #fff; font-weight: 600; cursor: pointer; }");
            out.println("    .error { color: #b91c1c; margin-bottom: 1rem; font-size: 0.875rem; }");
            out.println("    .hint { margin-top: 1rem; font-size: 0.75rem; color: #71717a; }");
            out.println("  </style>");
            out.println("</head>");
            out.println("<body>");
            out.println("  <main class=\"card\">");
            out.println("    <h1>AzumiDesigns</h1>");
            out.println("    <p>Sign in to continue</p>");
            if (showError) {
                out.println("    <p class=\"error\">Invalid username or password.</p>");
            }
            out.println("    <form method=\"post\" action=\"login\">");
            if (!redirect.isEmpty()) {
                out.println("      <input type=\"hidden\" name=\"redirect\" value=\"" + escape(redirect) + "\">");
            }
            out.println("      <label for=\"username\">Username</label>");
            out.println("      <input id=\"username\" name=\"username\" autocomplete=\"username\" required>");
            out.println("      <label for=\"password\">Password</label>");
            out.println("      <input id=\"password\" name=\"password\" type=\"password\" autocomplete=\"current-password\" required>");
            out.println("      <button type=\"submit\">Sign in</button>");
            out.println("    </form>");
            out.println("    <p class=\"hint\">Default users: admin / teammember</p>");
            out.println("  </main>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private String escape(String value) {
        return value.replace("&", "&amp;")
                .replace("\"", "&quot;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
