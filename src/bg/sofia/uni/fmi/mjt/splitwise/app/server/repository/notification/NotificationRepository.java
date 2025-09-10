package bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.notification;

import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.Group;

public interface NotificationRepository {
    void addNotification(String runner, String message, Group group);

    String getUnreadNotifications(String username);
}
