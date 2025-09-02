package bg.sofia.uni.fmi.mjt.splitwise.app.server.repository;

import java.nio.file.Path;
import java.nio.file.Paths;

public enum RepositoryPath {
    USERS(Paths.get("resources", "users.json")),
    GROUPS_BY_NAME(Paths.get("resources", "groupsName.json")),
    GROUPS_BY_USER(Paths.get("resources", "groupsUser.json")),
    EXPENSES(Paths.get("resources", "expenses.json")),
    BALANCES(Paths.get("resources", "balances.json")),
    NOTIFICATIONS(Paths.get("resources", "notifications.json"));

    private final Path path;

    RepositoryPath(Path path) {
        this.path = path;
    }

    public String getPath() {
        return path.toString();
    }

    public static int getFilesCount() {
        return RepositoryPath.values().length;
    }
}
