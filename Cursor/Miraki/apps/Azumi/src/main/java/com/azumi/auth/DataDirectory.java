package com.azumi.auth;

import jakarta.servlet.ServletContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class DataDirectory {

    private DataDirectory() {
    }

    public static Path resolve(ServletContext context) {
        String configured = System.getProperty("azumi.database.path", "data/local-db");
        Path path = Paths.get(configured);

        if (!path.isAbsolute()) {
            String normalized = configured.startsWith("./") ? configured.substring(2) : configured;
            Path appRoot = resolveAppRoot();
            path = appRoot.resolve(normalized);
        }

        return path;
    }

    private static Path resolveAppRoot() {
        String catalinaBase = System.getProperty("catalina.base");
        if (catalinaBase != null) {
            Path runtime = Paths.get(catalinaBase);
            if (runtime.getParent() != null && "tomcat".equals(runtime.getParent().getFileName().toString())) {
                Path appRoot = runtime.getParent().getParent();
                if (Files.isDirectory(appRoot)) {
                    return appRoot.toAbsolutePath().normalize();
                }
            }
        }

        return Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
    }
}
