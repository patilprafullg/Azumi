package com.azumi.auth;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebListener
public class UserStoreListener implements ServletContextListener {

    public static final String CONTEXT_KEY = "azumi.userStore";
    private static final Logger LOGGER = Logger.getLogger(UserStoreListener.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            UserStore store = UserStore.load(event.getServletContext());
            event.getServletContext().setAttribute(CONTEXT_KEY, store);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize user store", e);
            throw new IllegalStateException("Failed to initialize user store", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        event.getServletContext().removeAttribute(CONTEXT_KEY);
    }

    public static UserStore get(jakarta.servlet.ServletContext context) {
        Object value = context.getAttribute(CONTEXT_KEY);
        if (value instanceof UserStore store) {
            return store;
        }
        throw new IllegalStateException("User store is not initialized");
    }
}
