package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.expense;

import bg.sofia.uni.fmi.mjt.splitwise.api.client.ExchangeRatesClient;
import bg.sofia.uni.fmi.mjt.splitwise.api.dto.conversion.ExchangeRates;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.expense.EqualSplitExpense;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.expense.Expense;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.expense.InMemoryExpenseRepository;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.group.InMemoryGroupRepository;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.notification.InMemoryNotificationRepository;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.user.InMemoryUserRepository;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.user.UserSessionManager;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.math.BigDecimal;
import java.nio.channels.SocketChannel;

public final class PaidCommand extends DebtStatusCommand {
    private static final int AMOUNT_IDX = 0;
    private static final int USERNAME_IDX = 1;

    private static final String SUCCESS_MESSAGE = "%s (%s) paid you %s %s\nCurrent status: %s";
    private static final String NOTIFICATION_MESSAGE = "%s approved your payment of %s %s.";

    public PaidCommand(String[] args, SocketChannel socket) {
        super(args, CommandType.PAID, socket);
    }

    @Override
    protected String executeCommand() throws ExpenseSplitException {
        User runner = UserSessionManager.getInstance().getLoggedUser(socket);
        User debited = InMemoryUserRepository.getInstance().getUserByUsername(args[USERNAME_IDX]);

        Expense expense =
            new EqualSplitExpense(new BigDecimal(args[AMOUNT_IDX]), runner.getUsername(), null, "Pay back");
        InMemoryExpenseRepository.getInstance().pay(expense, debited.getUsername());

        ExchangeRates exchangeRates = ExchangeRatesClient.getInstance().getExchangeRates();
        String currStatus = getDebtStatus(runner, debited);

        InMemoryNotificationRepository.getInstance()
            .addNotification(runner.getUsername(),
                String.format(NOTIFICATION_MESSAGE, runner.getNickname(), args[AMOUNT_IDX], debited.getCurrency()),
                InMemoryGroupRepository.getInstance().getGroup(runner.getUsername(), debited.getUsername()));

        return String.format(SUCCESS_MESSAGE, debited.getNickname(), debited.getUsername(),
            exchangeRates.convert(new BigDecimal(args[AMOUNT_IDX]), runner.getCurrency()),
            runner.getCurrency(), currStatus);
    }
}
