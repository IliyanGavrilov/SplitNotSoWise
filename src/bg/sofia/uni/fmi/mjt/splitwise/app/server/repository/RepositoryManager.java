package bg.sofia.uni.fmi.mjt.splitwise.app.server.repository;

import bg.sofia.uni.fmi.mjt.splitwise.api.client.ExchangeRatesClient;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.expense.ExpenseRepository;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.group.GroupRepository;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.notification.NotificationRepository;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.user.UserRepository;

public record RepositoryManager(ExpenseRepository expenseRepository, GroupRepository groupRepository,
                                NotificationRepository notificationRepository,
                                bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.session.UserSessionManager sessionManager,
                                UserRepository userRepository, ExchangeRatesClient exchangeRatesClient) {

}
