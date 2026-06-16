package com.azumi;

import com.azumi.auth.AuthSession;
import com.azumi.auth.Role;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "HelloWorldServlet", urlPatterns = {"/hello"})
public class HelloWorldServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String username = AuthSession.getUsername(request.getSession(false));
        Role role = AuthSession.getRole(request.getSession(false));

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang=\"en\">");
            out.println("<head>");
            out.println("  <meta charset=\"UTF-8\">");
            out.println("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">");
            out.println("  <title>AzumiDesigns</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("  <h1>Hello World from AzumiDesigns</h1>");
            out.println("  <p>Local Tomcat deployment is running.</p>");
            out.println("  <p>Signed in as <strong>" + escape(username) + "</strong> (" + escape(role.getId()) + ").</p>");
            if (role == Role.ADMIN) {
                out.println("  <p>Admin access: full dashboard controls are available.</p>");
            } else {
                out.println("  <p>Team member access: standard dashboard features are available.</p>");
            }
            out.println("  <p><a href=\"" + request.getContextPath() + "/\">Back to home</a> | <a href=\"" + request.getContextPath() + "/logout\">Sign out</a></p>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private String escape(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;");
    }
}
