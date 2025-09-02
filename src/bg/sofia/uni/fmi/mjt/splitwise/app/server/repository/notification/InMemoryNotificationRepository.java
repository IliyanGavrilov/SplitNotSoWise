package bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.notification;

import bg.sofia.uni.fmi.mjt.splitwise.app.server.ExpenseServer;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.Group;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.RepositorySaver;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.user.UserSessionManager;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class InMemoryNotificationRepository implements NotificationRepository {
    private static final String NO_NOTIFICATION_MESSAGE = "No notifications to show.";
    private static final String NOTIFICATION_MESSAGE = "*** Notifications ***";
    private static final String FRIENDSHIP_GROUP_NAME = "Friends";
    private static final String NOTIFICATIONS_DELIMITER = "\n";

    private final Map<String, Map<String, Queue<String>>> userNotifications; // Outer key - user, inner - group name
    private static InMemoryNotificationRepository instance;

    private void notify(String message, String groupName, String username) {
        try {
            ExpenseServer.getInstance()
                .writeClientOutput(UserSessionManager.getInstance().getSocketChannel(username),
                    NOTIFICATION_MESSAGE + '\n' + message);
        } catch (ExpenseSplitException _) {
            userNotifications.computeIfAbsent(username, _ -> new HashMap<>())
                .computeIfAbsent(groupName, _ -> new LinkedList<>()).add(message);

            RepositorySaver.saveNotifications(userNotifications);
        } catch (IOException e) {
            System.err.printf("Failed to notify user %s%n", e.getMessage());
        }
    }

    private InMemoryNotificationRepository() {
        userNotifications = RepositorySaver.loadNotifications();
    }

    private Map<String, Queue<String>> clearNotifications(String username) {
        return userNotifications.remove(username);
    }

    private String printUnreadNotifications(Map<String, Queue<String>> notifications) {
        StringBuilder result = new StringBuilder();

        for (String key : notifications.keySet()) {
            result.append(key).append(":\n");
            result.append(String.join(NOTIFICATIONS_DELIMITER, notifications.get(key)));
        }

        return result.toString();
    }

    public static InMemoryNotificationRepository getInstance() {
        if (instance == null) {
            instance = new InMemoryNotificationRepository();
        }

        return instance;
    }

    public void addNotification(String runner, String message, Group group) {
        String groupName = group.isFriendshipGroup() ? FRIENDSHIP_GROUP_NAME : group.name();
        Set<String> users = group.usersExcept(runner);

        for (String username : users) {
            notify(message, groupName, username);
        }
    }

    public String getUnreadNotifications(String username) {
        Map<String, Queue<String>> result = clearNotifications(username);

        return result != null ? printUnreadNotifications(result) : NO_NOTIFICATION_MESSAGE;
    }
}
