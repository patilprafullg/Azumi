package com.azumi.auth;

import jakarta.servlet.ServletContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class UserStore {

    private static final String STORE_FILE = "users.properties";
    private static final String KEY_ROLE = ".role";
    private static final String KEY_HASH = ".hash";
    private static final String KEY_SALT = ".salt";

    private final Path storePath;
    private final Map<String, UserAccount> users = new LinkedHashMap<>();

    public UserStore(Path dataDirectory) {
        this.storePath = dataDirectory.resolve(STORE_FILE);
    }

    public static UserStore load(ServletContext context) throws IOException {
        Path dataDirectory = DataDirectory.resolve(context);
        Files.createDirectories(dataDirectory);

        UserStore store = new UserStore(dataDirectory);
        if (Files.exists(store.storePath)) {
            store.readFromDisk();
        } else {
            store.seedDefaults();
            store.writeToDisk();
        }
        return store;
    }

    public Optional<UserAccount> authenticate(String username, String password) {
        UserAccount account = users.get(username);
        if (account == null) {
            return Optional.empty();
        }
        if (!PasswordHasher.verify(password, account.salt(), account.passwordHash())) {
            return Optional.empty();
        }
        return Optional.of(account);
    }

    public Optional<UserAccount> find(String username) {
        return Optional.ofNullable(users.get(username));
    }

    private void seedDefaults() {
        addDefaultUser("admin", "admin", Role.ADMIN);
        addDefaultUser("teammember", "member", Role.TEAM_MEMBER);
    }

    private void addDefaultUser(String username, String password, Role role) {
        String salt = PasswordHasher.generateSalt();
        String hash = PasswordHasher.hash(password, salt);
        users.put(username, new UserAccount(username, hash, salt, role));
    }

    private void readFromDisk() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(storePath)) {
            properties.load(input);
        }

        for (String key : properties.stringPropertyNames()) {
            if (!key.endsWith(KEY_ROLE)) {
                continue;
            }

            String username = key.substring(0, key.length() - KEY_ROLE.length());
            String roleId = properties.getProperty(key);
            String hash = properties.getProperty(username + KEY_HASH);
            String salt = properties.getProperty(username + KEY_SALT);

            if (roleId == null || hash == null || salt == null) {
                continue;
            }

            users.put(username, new UserAccount(username, hash, salt, Role.fromId(roleId)));
        }
    }

    private void writeToDisk() throws IOException {
        Properties properties = new Properties();
        for (UserAccount account : users.values()) {
            String username = account.username();
            properties.setProperty(username + KEY_ROLE, account.role().getId());
            properties.setProperty(username + KEY_HASH, account.passwordHash());
            properties.setProperty(username + KEY_SALT, account.salt());
        }

        try (OutputStream output = Files.newOutputStream(storePath)) {
            properties.store(output, "AzumiDesigns user credentials (hashed)");
        }
    }
}
