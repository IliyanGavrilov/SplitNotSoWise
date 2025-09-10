package bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.notification;

import bg.sofia.uni.fmi.mjt.splitwise.app.server.ExpenseServer;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.Group;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.RepositorySaver;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.session.UserSessionManager;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public final class InMemoryNotificationRepository implements NotificationRepository {
    private static final String NO_NOTIFICATION_MESSAGE = "No notifications to show.";
    private static final String NOTIFICATION_MESSAGE = "*** Notifications ***";
    private static final String FRIENDSHIP_GROUP_NAME = "Friends";
    private static final String NOTIFICATIONS_DELIMITER = "\n";

    private final Map<String, Map<String, Queue<String>>> userNotifications; // Outer key - user, inner - group name
    private final UserSessionManager userSessionManager;

    private void notify(String message, String groupName, String username) {
        try {
            ExpenseServer.getInstance()
                .writeClientOutput(userSessionManager.getSocketChannel(username),
                    formatSingleNotification(groupName, message));
        } catch (ExpenseSplitException _) {
            userNotifications
                .computeIfAbsent(username, _ -> new HashMap<>())
                .computeIfAbsent(groupName, _ -> new LinkedList<>())
                .add(message);

            RepositorySaver.saveNotifications(userNotifications);
        } catch (IOException e) {
            System.err.printf("Failed to notify user %s%n", e.getMessage());
        }
    }

    private String formatSingleNotification(String groupName, String message) {
        return "*** Notifications ***\n" + groupName + ":\n" + message;
    }

    public InMemoryNotificationRepository(UserSessionManager userSessionManager) {
        userNotifications = RepositorySaver.loadNotifications();
        this.userSessionManager = userSessionManager;
    }

    private Map<String, Queue<String>> clearNotifications(String username) {
        return userNotifications.remove(username);
    }

    private String printUnreadNotifications(Map<String, Queue<String>> notifications) {
        StringBuilder result = new StringBuilder();
        result.append("*** Notifications ***\n");

        for (Map.Entry<String, Queue<String>> entry : notifications.entrySet()) {
            String groupName = entry.getKey();
            result.append(groupName).append(":\n");

            for (String msg : entry.getValue()) {
                result.append(msg).append('\n');
            }
            result.append('\n'); // blank line between groups
        }

        return result.toString().trim(); // remove last newline
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
