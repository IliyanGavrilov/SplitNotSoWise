package bg.sofia.uni.fmi.mjt.splitwise.app.command.commands.expense;

import bg.sofia.uni.fmi.mjt.splitwise.api.client.ExchangeRatesClient;
import bg.sofia.uni.fmi.mjt.splitwise.api.dto.conversion.ExchangeRates;
import bg.sofia.uni.fmi.mjt.splitwise.app.command.CommandType;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.expense.EqualSplitExpense;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.Group;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.model.user.User;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.expense.InMemoryExpenseRepository;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.notification.InMemoryNotificationRepository;
import bg.sofia.uni.fmi.mjt.splitwise.app.server.repository.user.UserSessionManager;
import bg.sofia.uni.fmi.mjt.splitwise.exception.exceptions.ExpenseSplitException;

import java.math.BigDecimal;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

public abstract sealed class SplitCommand extends DebtStatusCommand permits SplitFriendshipCommand, SplitGroupCommand {
    protected static final int AMOUNT_IDX = 0;
    protected static final int NAME_IDX = 1;
    protected static final int REASON_FOR_PAYMENT_IDX = 2;
    protected static final String REASON_DELIMITER = " ";
    protected static final String INVALID_GROUP_MESSAGE = "Given %s does not exist!";

    private static final String SUCCESS_MESSAGE = "Split %s %s between you and %s\nCurrent status: %s";
    private static final String NOTIFICATION_MESSAGE = "%s [%s].";

    public SplitCommand(String[] args, CommandType type, SocketChannel socket) {
        super(args, type, socket);
    }

    @Override
    protected String executeCommand() throws ExpenseSplitException {
        User runner = UserSessionManager.getInstance().getLoggedUser(socket);

        Group group = getGroup(runner);

        if (group == null) {
            throw new ExpenseSplitException(invalidGroupMessage());
        }

        String reason = String.join(REASON_DELIMITER, Arrays.copyOfRange(args, REASON_FOR_PAYMENT_IDX, args.length));

        InMemoryExpenseRepository.getInstance()
            .addExpense(new EqualSplitExpense(new BigDecimal(args[AMOUNT_IDX]), runner.getUsername(), group, reason));

        ExchangeRates exchangeRates = ExchangeRatesClient.getInstance().getExchangeRates();
        BigDecimal amount = exchangeRates.convert(new BigDecimal(args[AMOUNT_IDX]), runner.getCurrency());

        InMemoryNotificationRepository.getInstance()
            .addNotification(runner.getUsername(),
                String.format(NOTIFICATION_MESSAGE, runner.getNickname(), args[AMOUNT_IDX], runner.getCurrency()),
                group);

        return String.format(SUCCESS_MESSAGE, amount, runner.getCurrency(), getName(group),
            getIdentifier(runner.getUsername(), group));
    }

    protected abstract Group getGroup(User runner) throws ExpenseSplitException;

    protected abstract String invalidGroupMessage();

    protected abstract String getName(Group group) throws ExpenseSplitException;

    protected abstract String getIdentifier(String runner, Group group) throws ExpenseSplitException;
}
